package com.github.pnowy.store.product.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

public class NewProductCommand {
    private String name;
    private BigDecimal price;

    public NewProductCommand(@NotEmpty String name, @Positive BigDecimal price) {
        this.name = requireNonNull(name);
        this.price = requireNonNull(price);
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
