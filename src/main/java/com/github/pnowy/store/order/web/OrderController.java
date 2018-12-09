package com.github.pnowy.store.order.web;

import com.github.pnowy.store.order.domain.CreateOrderCommand;
import com.github.pnowy.store.order.domain.OrderAggregateRoot;
import com.github.pnowy.store.order.domain.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderRes createOrder(@RequestBody @Valid OrderReq orderReq) {
        final OrderAggregateRoot aggregateRoot = orderService.createOrder(new CreateOrderCommand(orderReq.getCustomerEmail(), orderReq.getProducts()));
        return new OrderRes(aggregateRoot);
    }

    @GetMapping("/{id}")
    public OrderRes getOrder(@PathVariable long id) {
        return new OrderRes(orderService.getOrder(id));
    }

}
