package org.wsd.app.comamnds;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class ReserveProductCommand {
    @TargetAggregateIdentifier
    private final String productId;
    private final String orderId;
    private final String userId;
    private final int quantity;
}
