package org.wsd.app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "payments")
public class PaymentEntity {
    @Id
    private String paymentId;
    @Column
    public String orderId;
}
