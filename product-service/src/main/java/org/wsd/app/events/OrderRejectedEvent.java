package org.wsd.app.events;

import lombok.Builder;
import lombok.Data;
import org.wsd.app.comamnds.OrderStatus;

@Data
@Builder
public class OrderRejectedEvent {
    private final String orderId;
    private final String reason;
    private final OrderStatus orderStatus = OrderStatus.REJECTED;
}
