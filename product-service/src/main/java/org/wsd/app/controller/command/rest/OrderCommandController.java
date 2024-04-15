package org.wsd.app.controller.command.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wsd.app.comamnds.CreateOrderCommand;
import org.wsd.app.comamnds.OrderStatus;
import org.wsd.app.payload.OrderRestModel;
import org.wsd.app.payload.OrderSummary;
import org.wsd.app.query.order.FindOrderQuery;

import java.util.UUID;

@Log4j2
@RestController
@Tag(name = "Order Command Controller")
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderCommandController {
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @PostMapping
    public OrderSummary createOrder(OrderRestModel orderRestModel) {

        final UUID orderId = UUID.randomUUID();

        final CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
                .orderId(orderId.toString())
                .productId(orderRestModel.getProductId())
                .addressId(orderRestModel.getAddressId())
                .quantity(orderRestModel.getQuantity())
                .userId(orderRestModel.getUserId())
                .orderStatus(OrderStatus.CREATED)
                .build();


        final FindOrderQuery findOrderQuery = FindOrderQuery.builder()
                .orderId(orderId.toString())
                .build();

        SubscriptionQueryResult<OrderSummary, OrderSummary> subscriptionQuery = queryGateway.subscriptionQuery(findOrderQuery, ResponseTypes.instanceOf(OrderSummary.class),
                ResponseTypes.instanceOf(OrderSummary.class));

        try {
            String id = this.commandGateway.sendAndWait(createOrderCommand);
            log.info("Created order with id {}", id);
            return subscriptionQuery.updates().blockFirst();
        } finally {
            subscriptionQuery.close();
        }
    }
}
