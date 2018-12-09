package com.github.pnowy.store.product.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

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

    Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
