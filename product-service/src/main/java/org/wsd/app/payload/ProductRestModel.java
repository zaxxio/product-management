package org.wsd.app.payload;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.wsd.app.domain.ProductEntity;

@Data
public class ProductRestModel {
    private String productId;
    private String productName;
    private Integer price;
    private Integer quantity;

    public static ProductRestModel fromEntity(ProductEntity productEntity) {
        ProductRestModel productRestModel = new ProductRestModel();
        productRestModel.setProductId(productEntity.getProductId());
        productRestModel.setProductName(productEntity.getProductName());
        productRestModel.setPrice(productEntity.getPrice());
        productRestModel.setQuantity(productEntity.getQuantity());
        return productRestModel;
    }
}
