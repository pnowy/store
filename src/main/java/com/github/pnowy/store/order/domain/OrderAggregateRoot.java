package com.github.pnowy.store.order.domain;

import com.github.pnowy.store.product.domain.Product;
import com.github.pnowy.store.product.domain.ProductService;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkState;

public class OrderAggregateRoot implements Serializable {

    private OrderAggregateRoot() {}

    private Long id;
    private String customerEmail;
    private BigDecimal totalPrice;
    private LocalDateTime createdDate;
    private List<Product> products;

    static OrderAggregateRoot of(CreateOrderCommand command, ProductService productService) {
        final OrderAggregateRoot aggregateRoot = new OrderAggregateRoot();
        final List<Product> productsToPlace = command.getProducts()
                .stream()
                .map(productService::getProduct)
                .collect(Collectors.toList());
        checkState(!productsToPlace.isEmpty(), "The products are required to place the order!");
        aggregateRoot.customerEmail = command.getCustomerEmail();
        aggregateRoot.products = productsToPlace;
        return aggregateRoot;
    }

    static OrderAggregateRoot of(Order order, ProductService productService) {
        final OrderAggregateRoot aggregateRoot = new OrderAggregateRoot();
        aggregateRoot.id = order.getId();
        aggregateRoot.customerEmail = order.getCustomerEmail();
        aggregateRoot.createdDate = order.getCreatedDate();
        aggregateRoot.totalPrice = order.getTotalPrice();
        aggregateRoot.products = order.getProducts().stream()
                .map(OrderItem::getProductRevision)
                .map(productService::getProduct)
                .collect(Collectors.toList());
        return aggregateRoot;
    }

    Order toNewOrder() {
        Order order = new Order();
        order.setCustomerEmail(customerEmail);
        order.setProducts(products.stream().map(OrderItem::new).collect(Collectors.toList()));
        order.setTotalPrice(products.stream().map(Product::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
        return order;
    }

    public Optional<Product> getProduct(long productId) {
        return this.products.stream().filter(p -> Objects.equals(p.getId(), productId)).findFirst();
    }

    public Long getId() {
        return id;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<Product> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
