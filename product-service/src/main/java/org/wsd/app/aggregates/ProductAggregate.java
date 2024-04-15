package org.wsd.app.aggregates;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.wsd.app.comamnds.CancelProductReservationCommand;
import org.wsd.app.comamnds.CreateProductCommand;
import org.wsd.app.comamnds.ReserveProductCommand;
import org.wsd.app.events.ProductCreatedEvent;
import org.wsd.app.events.ProductReservationCancelledEvent;
import org.wsd.app.events.ProductReservedEvent;

@Data
@Aggregate(
        repository = "productAggregateRepository",
        snapshotTriggerDefinition = "productSnapshotTriggerDefinition"
)
@NoArgsConstructor
public class ProductAggregate {

    @AggregateIdentifier
    private String productId;
    private String productName;
    private Integer price;
    private Integer quantity;

    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand) {

        if (createProductCommand.getProductName() == null || createProductCommand.getProductName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (createProductCommand.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }

        final ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();
        productCreatedEvent.setProductId(createProductCommand.getProductId());
        productCreatedEvent.setProductName(createProductCommand.getProductName());
        productCreatedEvent.setPrice(createProductCommand.getPrice());
        productCreatedEvent.setQuantity(createProductCommand.getQuantity());

        AggregateLifecycle.apply(productCreatedEvent);
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent productCreatedEvent) {
        this.productId = productCreatedEvent.getProductId();
        this.productName = productCreatedEvent.getProductName();
        this.price = productCreatedEvent.getPrice();
        this.quantity = productCreatedEvent.getQuantity();
    }

    @CommandHandler
    public void on(ReserveProductCommand reserveProductCommand) {
        if (this.quantity < reserveProductCommand.getQuantity()) {
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

    @CommandHandler
    public void on(CancelProductReservationCommand cancelProductReservationCommand) {
        AggregateLifecycle.apply(
                ProductReservationCancelledEvent.builder()
                        .productId(cancelProductReservationCommand.getProductId())
                        .userId(cancelProductReservationCommand.getUserId())
                        .orderId(cancelProductReservationCommand.getOrderId())
                        .quantity(cancelProductReservationCommand.getQuantity())
                        .reason(cancelProductReservationCommand.getReason())
                        .build()
        );
    }

    @EventSourcingHandler
    public void on(ProductReservationCancelledEvent productReservationCancelledEvent) {
        this.quantity += productReservationCancelledEvent.getQuantity();
    }


    @EventSourcingHandler
    public void on(ProductReservedEvent productReservedEvent) {
        this.quantity -= productReservedEvent.getQuantity();
    }


}
