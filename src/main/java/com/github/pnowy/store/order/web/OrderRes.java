package com.github.pnowy.store.order.web;

import com.github.pnowy.store.order.domain.OrderAggregateRoot;
import com.github.pnowy.store.product.web.ProductRes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.stream.Collectors;

public class OrderRes {

    public OrderRes(OrderAggregateRoot aggregateRoot) {
        this.id = aggregateRoot.getId();
        this.createdDate = aggregateRoot.getCreatedDate();
        this.customerEmail = aggregateRoot.getCustomerEmail();
        this.totalPrice = aggregateRoot.getTotalPrice();
        this.products = aggregateRoot.getProducts().stream().map(ProductRes::new).collect(Collectors.toList());
    }

    private Long id;
    private Instant createdDate;
    private String customerEmail;
    private BigDecimal totalPrice;
    private Collection<ProductRes> products;

    public Long getId() {
        return id;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public Collection<ProductRes> getProducts() {
        return products;
    }
}
