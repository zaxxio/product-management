package org.wsd.app.aggregates;

import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.wsd.app.comamnds.CreateOrderCommand;
import org.wsd.app.comamnds.OrderStatus;
import org.wsd.app.comamnds.ReserveProductCommand;
import org.wsd.app.events.OrderCreatedEvent;
import org.wsd.app.events.ProductReservedEvent;

@Aggregate
@NoArgsConstructor
public class OrderAggregate {
    @AggregateIdentifier
    public String orderId;
    private String productId;
    private String userId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;

    @CommandHandler(payloadType = CreateOrderCommand.class)
    public OrderAggregate(CreateOrderCommand createOrderCommand) {
        final OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        orderCreatedEvent.setProductId(createOrderCommand.getProductId());
        orderCreatedEvent.setQuantity(createOrderCommand.getQuantity());
        orderCreatedEvent.setUserId(createOrderCommand.getUserId());
        orderCreatedEvent.setOrderId(createOrderCommand.getOrderId());
        orderCreatedEvent.setOrderStatus(createOrderCommand.getOrderStatus());
        orderCreatedEvent.setAddressId(createOrderCommand.getAddressId());
        AggregateLifecycle.apply(orderCreatedEvent);
    }


    @CommandHandler(payloadType = ReserveProductCommand.class)
    public void on(ReserveProductCommand reserveProductCommand) {
        if (quantity < reserveProductCommand.getQuantity()) {
            throw new IllegalArgumentException("Reserve product quantity is insufficient");
        }

        final ProductReservedEvent productReservedEvent = ProductReservedEvent.builder()
                .productId(reserveProductCommand.getProductId())
                .orderId(reserveProductCommand.getOrderId())
                .userId(reserveProductCommand.getUserId())
                .quantity(reserveProductCommand.getQuantity())
                .build();

        AggregateLifecycle.apply(productReservedEvent);
    }


    @EventSourcingHandler
    public void on(ProductReservedEvent productReservedEvent) {
        this.quantity -= productReservedEvent.getQuantity();
    }


    @EventSourcingHandler
    public void on(OrderCreatedEvent orderCreatedEvent) {
        this.orderId = orderCreatedEvent.getOrderId();
        this.productId = orderCreatedEvent.getProductId();
        this.userId = orderCreatedEvent.getUserId();
        this.quantity = orderCreatedEvent.getQuantity();
        this.addressId = orderCreatedEvent.getAddressId();
        this.orderStatus = orderCreatedEvent.getOrderStatus();
    }

}
