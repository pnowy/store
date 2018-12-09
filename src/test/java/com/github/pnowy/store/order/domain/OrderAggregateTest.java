package com.github.pnowy.store.order.domain;

import com.github.pnowy.store.product.domain.Product;
import com.github.pnowy.store.product.domain.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class OrderAggregateTest {

    @Test
    void shouldAcceptOnlyNonEmptyOrders() {
        // setup
        final ProductService productService = Mockito.mock(ProductService.class);
        Mockito.when(productService.getProduct(Mockito.anyLong())).then((Answer<Product>) invocation -> {
            final Long id = invocation.getArgument(0);
            return new Product(id, "Nae", BigDecimal.TEN);
        });

        // when
        final Throwable throwable = catchThrowable(() -> OrderAggregateRoot.of(new CreateOrderCommand("pnowy@localhost.com", Collections.emptyList()), productService));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }
}
