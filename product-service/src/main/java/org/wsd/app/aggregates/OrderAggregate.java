package org.wsd.app.aggregates;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.wsd.app.comamnds.ApproveOrderCommand;
import org.wsd.app.comamnds.CreateOrderCommand;
import org.wsd.app.comamnds.OrderStatus;
import org.wsd.app.comamnds.ReserveProductCommand;
import org.wsd.app.events.OrderApprovedEvent;
import org.wsd.app.events.OrderCreatedEvent;
import org.wsd.app.events.ProductReservedEvent;

@Data
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

    @CommandHandler
    public OrderAggregate(CreateOrderCommand createOrderCommand) {
        AggregateLifecycle.apply(OrderCreatedEvent.builder()
                .orderId(createOrderCommand.getOrderId())
                .productId(createOrderCommand.getProductId())
                .userId(createOrderCommand.getUserId())
                .quantity(createOrderCommand.getQuantity())
                .addressId(createOrderCommand.getAddressId())
                .orderStatus(OrderStatus.CREATED)
                .build()
        );
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

    @CommandHandler
    public void on(ApproveOrderCommand approveOrderCommand) {
        final OrderApprovedEvent orderApprovedEvent = OrderApprovedEvent.builder()
                .orderId(approveOrderCommand.getOrderId())
                .build();
        AggregateLifecycle.apply(orderApprovedEvent);
    }

    @EventSourcingHandler
    public void on(OrderApprovedEvent orderApprovedEvent) {
        this.orderStatus = orderApprovedEvent.getOrderStatus();
    }

}
