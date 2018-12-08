package com.github.pnowy.store.product.web;

import com.github.pnowy.store.product.domain.Product;

import java.math.BigDecimal;

public class ProductRes {

    public ProductRes(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
    }

    private Long id;
    private String name;
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

}
