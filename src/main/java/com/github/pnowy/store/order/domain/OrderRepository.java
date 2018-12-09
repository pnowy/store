package com.github.pnowy.store.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Collection<Order> findByCreatedDateAfterAndCreatedDateBefore(LocalDateTime from, LocalDateTime to);
}
