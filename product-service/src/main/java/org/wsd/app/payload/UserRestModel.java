package org.wsd.app.payload;

import lombok.Builder;
import lombok.Data;
import org.wsd.app.model.PaymentDetails;

@Data
@Builder
public class UserRestModel {
    private final String firstName;
    private final String lastName;
    private final String userId;
    private final PaymentDetails paymentDetails;
}
