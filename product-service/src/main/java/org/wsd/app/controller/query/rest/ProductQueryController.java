package org.wsd.app.controller.query.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wsd.app.payload.ProductRestModel;
import org.wsd.app.query.product.FindProductsQuery;

import java.util.List;

@Tag(name = "Product Query Controller")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductQueryController {
    private final QueryGateway queryGateway;
    @GetMapping
    public List<ProductRestModel> getProduct() {
        final FindProductsQuery findProductsQuery = new FindProductsQuery();
        return this.queryGateway.query(findProductsQuery, ResponseTypes.multipleInstancesOf(ProductRestModel.class)).join();
    }
}
