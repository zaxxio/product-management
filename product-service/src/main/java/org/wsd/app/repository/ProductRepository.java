package org.wsd.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wsd.app.domain.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {
}
