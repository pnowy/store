package com.github.pnowy.store.product;

import com.github.pnowy.store.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

interface ProductRepository extends JpaRepository<Product, Long> {
}
