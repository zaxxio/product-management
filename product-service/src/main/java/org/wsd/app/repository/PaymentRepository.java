package org.wsd.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wsd.app.domain.PaymentEntity;

public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {
}
