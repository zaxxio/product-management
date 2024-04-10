package org.wsd.app.projection;

import lombok.extern.log4j.Log4j2;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.wsd.app.model.PaymentDetails;
import org.wsd.app.payload.UserRestModel;
import org.wsd.app.query.user.FetchUserPaymentDetailsQuery;

@Log4j2
@Component
@ProcessingGroup("user-group")
@Transactional(rollbackFor = Exception.class)
public class UserProjection {

    @QueryHandler
    public UserRestModel on(FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery) {
        return UserRestModel.builder()
                .userId(fetchUserPaymentDetailsQuery.getUserId())
                .firstName("Partha")
                .lastName("Sutradhar")
                .paymentDetails(PaymentDetails.builder()
                        .name("Partha Sutradhar")
                        .cardNumber("12312732131728312")
                        .validUntilMonth(12)
                        .validUntilYear(2028)
                        .cvv("890")
                        .build())
                .build();
    }

}
