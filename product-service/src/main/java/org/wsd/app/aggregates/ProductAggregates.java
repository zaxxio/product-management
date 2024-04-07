package org.wsd.app.aggregates;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.wsd.app.comamnds.CreateProductCommand;
import org.wsd.app.events.ProductCreatedEvent;

@Aggregate
public class ProductAggregates {

    @AggregateIdentifier
    private String productId;
    private String productName;
    private Integer price;
    private Integer quantity;

    public ProductAggregates() {}

    @CommandHandler
    public ProductAggregates(CreateProductCommand createProductCommand) {

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

}
