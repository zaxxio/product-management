package org.wsd.app.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.wsd.app.comamnds.OrderStatus;

@Data
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @Column(unique = true)
    public String orderId;
    private String productId;
    private String userId;
    private int quantity;
    private String addressId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
