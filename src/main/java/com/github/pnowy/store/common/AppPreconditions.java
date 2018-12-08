package com.github.pnowy.store.common;

import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class AppPreconditions {
    private AppPreconditions() {}

    /**
     * Checks if provided expression is true. In case of false the provided exception by supplier will be thrown. It's better
     * to provide the supplier in case of more complicated exception because we can avoid to create that exception until this is
     * really necessary.
     *
     * @param expression a boolean expression
     * @param exceptionToThrowSupplier exception supplier
     */
    public static void check(final boolean expression, Supplier<? extends RuntimeException> exceptionToThrowSupplier) {
        if (!expression) {
            throw requireNonNull(exceptionToThrowSupplier).get();
        }
    }
}
