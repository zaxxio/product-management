package org.wsd.app.controller.command.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.*;
import org.wsd.app.comamnds.CreateProductCommand;
import org.wsd.app.payload.ProductRestModel;

import java.util.UUID;


@Log4j2
@RestController
@Tag(name = "Product Command Controller")
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
        String id = commandGateway.sendAndWait(createProductCommand);
        log.info("Created product with id {}", id);
        return id;
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
