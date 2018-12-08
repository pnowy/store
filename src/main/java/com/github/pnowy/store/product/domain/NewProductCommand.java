package com.github.pnowy.store.product.domain;

import com.google.common.base.Preconditions;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

import static java.util.Objects.requireNonNull;

public class NewProductCommand implements ProductCommand {
    private String name;
    private BigDecimal price;

    public NewProductCommand(@NotEmpty String name, @Positive BigDecimal price) {
        this.name = requireNonNull(name);
        this.price = requireNonNull(price);
    }

    @Override
    public void applyState(Product product) {
        Preconditions.checkArgument(Objects.isNull(product.getId()), "Cannot apply state for persisted product!");
        product.setPrice(this.price);
        product.setName(this.name);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NewProductCommand.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("price=" + price)
                .toString();
    }
}
