package com.github.pnowy.store.order.domain;

import com.github.pnowy.store.product.domain.*;
import com.google.common.collect.Lists;
import org.javers.core.JaversBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class OrderServiceTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;

    private ProductService productService;
    private OrderService orderService;
    private List<Product> availableItems;

    private Product sdd;
    private Product hdd;
    private Product mouse;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository, JaversBuilder.javers().build());
        sdd = productService.createProduct(new NewProductCommand("SDD", BigDecimal.TEN));
        hdd = productService.createProduct(new NewProductCommand("HDD", BigDecimal.ONE));
        mouse = productService.createProduct(new NewProductCommand("Mouse", BigDecimal.valueOf(7.0)));

        orderService = new OrderService(orderRepository, productService);
        availableItems = Lists.newArrayList(sdd, hdd, mouse);
    }

    @Test
    void shouldCreateNewOrder() {
        // given
        final BigDecimal totalPriceOfAvailableItems = availableItems.stream().map(Product::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        final CreateOrderCommand createOrderCommand = new CreateOrderCommand("pnowy@localhost.com", availableItems.stream().map(Product::getId).collect(Collectors.toList()));

        // when
        final OrderAggregateRoot createdRoot = orderService.createOrder(createOrderCommand);
        updateProductPrice();

        // then
        final OrderAggregateRoot orderAggregateRoot = orderService.getOrder(createdRoot.getId());
        assertThat(orderAggregateRoot).isNotNull();
        assertThat(orderAggregateRoot.getTotalPrice()).isEqualTo(totalPriceOfAvailableItems);
        assertThat(orderAggregateRoot.getCreatedDate()).isBefore(LocalDateTime.now());
    }

    @Test
    void shouldFindOrders() {
        // setup
        final CreateOrderCommand createOrderCommand = new CreateOrderCommand("pnowy@localhost.com", availableItems.stream().map(Product::getId).collect(Collectors.toList()));
        orderService.createOrder(createOrderCommand);

        // when
        final Collection<OrderAggregateRoot> orders = orderService.searchOrders(LocalDateTime.now().minusDays(7), LocalDateTime.now());

        // then
        assertThat(orders).hasSize(1);
    }

    private void updateProductPrice() {
        productService.updateProduct(new ModifyProductCommand(sdd.getId(), sdd.getName(), sdd.getPrice().add(BigDecimal.ONE)));
    }
}
