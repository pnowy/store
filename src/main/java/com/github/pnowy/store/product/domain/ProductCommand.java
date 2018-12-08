package com.github.pnowy.store.product.domain;

public interface ProductCommand {
    void applyState(Product product);
}
