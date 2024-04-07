package org.wsd.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wsd.app.domain.ProductLookupEntity;

public interface ProductLookRepository extends JpaRepository<ProductLookupEntity, String> {
}
