package org.wsd.app.projection;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.wsd.app.comamnds.OrderStatus;
import org.wsd.app.domain.OrderEntity;
import org.wsd.app.domain.ProductEntity;
import org.wsd.app.events.OrderApprovedEvent;
import org.wsd.app.events.OrderCreatedEvent;
import org.wsd.app.events.OrderRejectedEvent;
import org.wsd.app.events.ProductReservedEvent;
import org.wsd.app.payload.OrderRestModel;
import org.wsd.app.query.order.FindOrdersQuery;
import org.wsd.app.repository.OrderRepository;
import org.wsd.app.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Log4j2
@Component
@Transactional(rollbackFor = Exception.class)
@ProcessingGroup(value = "order-group")
@RequiredArgsConstructor
public class OrderProjection {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @EventHandler
    public void handle(OrderCreatedEvent orderCreatedEvent) {

        final OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(orderCreatedEvent.getOrderId());
        orderEntity.setProductId(orderCreatedEvent.getProductId());
        orderEntity.setQuantity(orderCreatedEvent.getQuantity());
        orderEntity.setUserId(orderCreatedEvent.getUserId());
        orderEntity.setAddressId(orderEntity.getAddressId());
        orderEntity.setOrderStatus(orderCreatedEvent.getOrderStatus());

        OrderEntity savedOrder = this.orderRepository.save(orderEntity);
        log.info("Saved order: {}", savedOrder);

    }

    @EventHandler
    public void handle(OrderApprovedEvent orderApprovedEvent) {
        Optional<OrderEntity> orderEntity = this.orderRepository.findById(orderApprovedEvent.getOrderId());
        if (orderEntity.isEmpty()) {
            return;
        }
        final OrderEntity order = orderEntity.get();
        order.setOrderStatus(OrderStatus.APPROVED);
        this.orderRepository.save(order);
    }

    @EventHandler
    public void handle(OrderRejectedEvent orderRejectedEvent){
        Optional<OrderEntity> orderEntity = this.orderRepository.findById(orderRejectedEvent.getOrderId());
        if (orderEntity.isEmpty()) {
            return;
        }
        final OrderEntity order = orderEntity.get();
        order.setOrderStatus(OrderStatus.REJECTED);
        this.orderRepository.save(order);
    }

    @QueryHandler
    public List<OrderRestModel> getOrders(FindOrdersQuery findOrdersQuery) {
        return this.orderRepository.findAll().stream().map(OrderRestModel::fromEntity).toList();
    }

}
