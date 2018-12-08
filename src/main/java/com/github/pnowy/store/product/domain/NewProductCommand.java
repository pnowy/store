package com.github.pnowy.store.product.domain;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class NewProductCommand {
    private String name;
    private BigDecimal price;

    public NewProductCommand(@NotEmpty String name, @Positive BigDecimal price) {
        this.name = requireNonNull(name);
        this.price = requireNonNull(price);
    }

    void applyState(Product product) {
        Preconditions.checkArgument(Objects.isNull(product.getId()), "Cannot apply state for persisted product!");
        product.setPrice(this.price);
        product.setName(this.name);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
