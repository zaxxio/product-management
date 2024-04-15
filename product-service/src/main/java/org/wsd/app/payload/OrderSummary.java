package org.wsd.app.payload;

import lombok.Value;
import org.wsd.app.comamnds.OrderStatus;

@Value
public class OrderSummary {
    private final String orderId;
    private final OrderStatus orderStatus;
}
