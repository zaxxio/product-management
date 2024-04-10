package org.wsd.app.query.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FetchUserPaymentDetailsQuery {
    private String userId;
}
