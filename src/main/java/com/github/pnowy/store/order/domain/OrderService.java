package com.github.pnowy.store.order.domain;

import com.github.pnowy.store.product.domain.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    @Transactional
    public OrderAggregateRoot createOrder(CreateOrderCommand command) {
        final OrderAggregateRoot aggregateRoot = OrderAggregateRoot.of(command, productService);
        final Order savedOrder = orderRepository.save(aggregateRoot.toNewOrder());
        log.info("Order has been created. Id={}, total={}", savedOrder.getId(), savedOrder.getTotalPrice());
        return OrderAggregateRoot.of(savedOrder, productService);
    }

    @Transactional(readOnly = true)
    public OrderAggregateRoot getOrder(long id) {
        Order order = orderRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("The product with id %s has not been found!", id)));
        return OrderAggregateRoot.of(order, productService);
    }

    @Transactional(readOnly = true)
    public Collection<OrderAggregateRoot> searchOrders(LocalDateTime from, LocalDateTime to) {
        final Collection<Order> orders = orderRepository.findByCreatedDateAfterAndCreatedDateBefore(from, to);
        return orders.stream()
                .map(order -> OrderAggregateRoot.of(order, productService))
                .collect(Collectors.toList());
    }


}
