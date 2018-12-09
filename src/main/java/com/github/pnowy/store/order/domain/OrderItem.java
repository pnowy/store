package com.github.pnowy.store.order.domain;

import com.github.pnowy.store.product.domain.Product;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class OrderItem implements Serializable {

    public OrderItem() {
    }

    OrderItem(Product product) {
        this.productRevision = product.getRevision();
    }

    @Column(name = "product_revision", nullable = false)
    private String productRevision;

    public String getProductRevision() {
        return productRevision;
    }

    public void setProductRevision(String productRevision) {
        this.productRevision = productRevision;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
