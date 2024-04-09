package org.wsd.app.projection;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.wsd.app.domain.ProductEntity;
import org.wsd.app.events.ProductCreatedEvent;
import org.wsd.app.payload.ProductRestModel;
import org.wsd.app.query.product.FindProductsQuery;
import org.wsd.app.repository.ProductRepository;

import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@ProcessingGroup("product-group")
public class ProductProjection {

    private final ProductRepository productRepository;

    @EventHandler
    public void on(ProductCreatedEvent productCreatedEvent) throws Exception {
        final ProductEntity productEntity = new ProductEntity();

        productEntity.setProductId(productCreatedEvent.getProductId());
        productEntity.setProductName(productCreatedEvent.getProductName());
        productEntity.setPrice(productCreatedEvent.getPrice());
        productEntity.setQuantity(productCreatedEvent.getQuantity());

        this.productRepository.save(productEntity);
    }

    @QueryHandler
    public List<ProductRestModel> getProducts(FindProductsQuery findProductsQuery) {
        return productRepository.findAll().stream().map(ProductRestModel::fromEntity).toList();
    }

}
