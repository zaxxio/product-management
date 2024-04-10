package org.wsd.app.events;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import org.wsd.app.comamnds.OrderStatus;

@Data
@Builder
public class OrderApprovedEvent {
    private final String orderId;
    private final OrderStatus orderStatus = OrderStatus.APPROVED;
}
