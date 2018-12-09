package com.github.pnowy.store.order.web;

import com.github.pnowy.store.order.domain.CreateOrderCommand;
import com.github.pnowy.store.order.domain.OrderAggregateRoot;
import com.github.pnowy.store.order.domain.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order")
@Api(tags = {"Order resource"})
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ApiOperation("Creates the new order with given products (the latest product version is assigned to the order)")
    public OrderRes createOrder(@RequestBody @Valid OrderReq orderReq) {
        final OrderAggregateRoot aggregateRoot = orderService.createOrder(new CreateOrderCommand(orderReq.getCustomerEmail(), orderReq.getProducts()));
        return new OrderRes(aggregateRoot);
    }

    @GetMapping("/{id}")
    @ApiOperation("Retrieve the order with all the items (products)")
    public OrderRes getOrder(@PathVariable long id) {
        return new OrderRes(orderService.getOrder(id));
    }

    @GetMapping
    @ApiOperation("Search the orders by date range (from - to). The parameters are not required. In case of lack the last 3 months are returned.")
    public ResponseEntity<Collection<OrderRes>> getOrders(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Optional<LocalDateTime> from,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam(required = false) Optional<LocalDateTime> to) {
        final LocalDateTime fromDate = from.orElse(LocalDateTime.now().minusMonths(3));
        final LocalDateTime toDate = to.orElse(LocalDateTime.now());

        if (toDate.isBefore(fromDate)) {
            throw new IllegalDateRangeException("The from date must be before date to");
        }
        final List<OrderRes> ordersList = orderService.searchOrders(fromDate, toDate)
                .stream()
                .map(OrderRes::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ordersList);
    }

}
