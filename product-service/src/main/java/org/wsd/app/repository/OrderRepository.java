package org.wsd.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wsd.app.domain.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {
}
