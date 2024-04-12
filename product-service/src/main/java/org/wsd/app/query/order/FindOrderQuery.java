package org.wsd.app.query.order;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindOrderQuery {
    private final String orderId;
}
