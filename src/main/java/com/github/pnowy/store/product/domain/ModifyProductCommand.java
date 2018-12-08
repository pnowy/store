package com.github.pnowy.store.product.domain;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class ModifyProductCommand {
    private Long id;
    private String name;
    private BigDecimal price;

    public ModifyProductCommand(Long id, String name, BigDecimal price) {
        this.id = requireNonNull(id);
        this.name = requireNonNull(name);
        this.price = requireNonNull(price);
    }

    void applyState(Product product) {
        Preconditions.checkArgument(Objects.equals(getId(), product.getId()), "Cannot apply state for different product that planned");
        product.setPrice(this.price);
        product.setName(this.name);
    }

    Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
