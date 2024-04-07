package org.wsd.app.comamnds;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class CreateProductCommand {
    @TargetAggregateIdentifier
    private final String productId;
    private final String productName;
    private final Integer price;
    private final Integer quantity;
}
