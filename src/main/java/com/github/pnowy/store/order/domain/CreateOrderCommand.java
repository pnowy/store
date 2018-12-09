package com.github.pnowy.store.order.domain;

import java.util.List;

public class CreateOrderCommand {
    private String customerEmail;
    private List<Long> products;

    public CreateOrderCommand(String customerEmail, List<Long> products) {
        this.customerEmail = customerEmail;
        this.products = products;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public List<Long> getProducts() {
        return products;
    }
}
