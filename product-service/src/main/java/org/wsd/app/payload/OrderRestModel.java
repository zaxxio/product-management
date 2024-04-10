package org.wsd.app.payload;

import lombok.Builder;
import lombok.Data;
import org.wsd.app.domain.OrderEntity;

@Data
@Builder
public class OrderRestModel {
    private String productId;
    private Integer quantity;
    private String addressId;
    private String userId;

    public static OrderRestModel fromEntity(OrderEntity orderEntity) {
        return OrderRestModel.builder()
                .productId(orderEntity.getProductId())
                .quantity(orderEntity.getQuantity())
                .addressId(orderEntity.getAddressId())
                .userId(orderEntity.getUserId())
                .build();
    }
}
