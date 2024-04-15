package org.wsd.app.controller.query.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;
import org.wsd.app.payload.OrderRestModel;
import org.wsd.app.query.order.FindAllOrdersQuery;

import java.util.List;

@Tag(name = "Order Query Controller")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderQueryController {
    private final QueryGateway queryGateway;

    @GetMapping
    public List<OrderRestModel> getOrders() {
        final FindAllOrdersQuery findAllOrdersQuery = new FindAllOrdersQuery();
        return queryGateway.query(findAllOrdersQuery, ResponseTypes.multipleInstancesOf(OrderRestModel.class)).join();
    }
    

}
