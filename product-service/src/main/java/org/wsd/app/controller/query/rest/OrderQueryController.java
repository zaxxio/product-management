package org.wsd.app.controller.query.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;
import org.wsd.app.payload.OrderRestModel;
import org.wsd.app.query.order.FindOrderQuery;
import org.wsd.app.query.order.FindOrdersQuery;

import java.util.List;

@Tag(name = "Order Query Controller")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderQueryController {
    private final QueryGateway queryGateway;

    @GetMapping
    public List<OrderRestModel> getOrders() {
        final FindOrdersQuery findOrdersQuery = new FindOrdersQuery();
        return queryGateway.query(findOrdersQuery, ResponseTypes.multipleInstancesOf(OrderRestModel.class)).join();
    }
    

}
