package org.wsd.app.events;

import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
public class ProductCreatedEvent {
    private String productId;
    private String productName;
    private Integer price;
    private Integer quantity;
}
