package com.github.pnowy.store.product.domain;

import com.google.common.base.Preconditions;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

import static java.util.Objects.requireNonNull;

public class ModifyProductCommand implements ProductCommand {
    private Long id;
    private String name;
    private BigDecimal price;

    public ModifyProductCommand(Long id, @NotEmpty String name, @Positive BigDecimal price) {
        this.id = requireNonNull(id);
        this.name = requireNonNull(name);
        this.price = requireNonNull(price);
    }

    public void applyState(Product product) {
        Preconditions.checkArgument(Objects.equals(getId(), product.getId()), "Cannot apply state for different product that planned");
        product.setPrice(this.price);
        product.setName(this.name);
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ModifyProductCommand.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("price=" + price)
                .toString();
    }
}
