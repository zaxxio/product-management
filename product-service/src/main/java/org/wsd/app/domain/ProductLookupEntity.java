package org.wsd.app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "product_look_table")
public class ProductLookupEntity implements Serializable {
    @Id
    @Column(name = "product_id", unique = true, nullable = false)
    private String productId;
}
