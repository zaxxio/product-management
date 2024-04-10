package org.wsd.app.controller.command.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wsd.app.comamnds.CreateOrderCommand;
import org.wsd.app.comamnds.OrderStatus;
import org.wsd.app.payload.OrderRestModel;

import java.util.UUID;

@Log4j2
@RestController
@Tag(name = "Order Command Controller")
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderCommandController {
    private final CommandGateway commandGateway;

    @PostMapping
    public String createOrder(OrderRestModel orderRestModel) {

        final CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
                .orderId(UUID.randomUUID().toString())
                .productId(orderRestModel.getProductId())
                .addressId(orderRestModel.getAddressId())
                .quantity(orderRestModel.getQuantity())
                .userId(orderRestModel.getUserId())
                .orderStatus(OrderStatus.CREATED)
                .build();

        String id = this.commandGateway.sendAndWait(createOrderCommand);
        log.info("Created order with id {}", id);
        return id;
    }
}
