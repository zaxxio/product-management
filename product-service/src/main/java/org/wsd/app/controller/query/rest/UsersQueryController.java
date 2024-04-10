package org.wsd.app.controller.query.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wsd.app.payload.UserRestModel;
import org.wsd.app.query.user.FetchUserPaymentDetailsQuery;

@RestController
@Tag(name = "Users Query Controller")
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UsersQueryController {
    private final QueryGateway queryGateway;
    @GetMapping("/{userId}")
    public UserRestModel getUserById(@PathVariable("userId") String userId) {
        final FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery = FetchUserPaymentDetailsQuery
                .builder()
                .userId(userId)
                .build();
        return queryGateway.query(fetchUserPaymentDetailsQuery, ResponseTypes.instanceOf(UserRestModel.class)).join();
    }
}
