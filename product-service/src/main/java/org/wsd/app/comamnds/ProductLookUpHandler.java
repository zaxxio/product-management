package org.wsd.app.comamnds;

import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.wsd.app.domain.ProductLookupEntity;
import org.wsd.app.events.ProductCreatedEvent;
import org.wsd.app.repository.ProductLookRepository;

@Component
@Transactional
@RequiredArgsConstructor
@ProcessingGroup("processing-group")
public class ProductLookUpHandler {
    private final ProductLookRepository productLookRepository;

    @EventHandler
    public void on(ProductCreatedEvent productCreatedEvent) {
        final ProductLookupEntity productLookupEntity = new ProductLookupEntity();
        productLookupEntity.setProductId(productCreatedEvent.getProductId());
        this.productLookRepository.save(productLookupEntity);
    }
}
