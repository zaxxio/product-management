package org.wsd.app.controller.command.rest;

import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.*;
import org.wsd.app.comamnds.CreateProductCommand;
import org.wsd.app.payload.ProductRestModel;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductCommandController {

    private final CommandGateway commandGateway;

    @PostMapping
    public String createProduct(@RequestBody ProductRestModel productRestModel) {
        final CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .productId(UUID.randomUUID().toString())
                .productName(productRestModel.getProductName())
                .price(productRestModel.getPrice())
                .quantity(productRestModel.getQuantity())
                .build();
        return commandGateway.sendAndWait(createProductCommand);
    }

    @PutMapping
    public String updateProduct() {
        return "HTTP Put Handled";
    }

    @DeleteMapping
    public String deleteProduct() {
        return "HTTP Put Handled";
    }

}
