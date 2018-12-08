package com.github.pnowy.store.product.web;

import com.github.pnowy.store.product.domain.ProductService;
import com.github.pnowy.store.product.domain.ModifyProductCommand;
import com.github.pnowy.store.product.domain.NewProductCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product")
@SuppressWarnings("unused")
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ProductRes createProduct(@Valid @RequestBody ProductReq product) {
        final NewProductCommand command = new NewProductCommand(product.getName(), product.getPrice());
        log.info("Create new product={}", command);
        return new ProductRes(productService.createProduct(command));
    }

    @PutMapping("/{id}")
    public ProductRes updateProduct(@PathVariable long id, @Valid @RequestBody ProductReq product) {
        final ModifyProductCommand modifyCommand = new ModifyProductCommand(id, product.getName(), product.getPrice());
        log.info("Update product={}", modifyCommand);
        return new ProductRes(productService.updateProduct(modifyCommand));
    }

    @GetMapping("/{id}")
    public ProductRes getProduct(@PathVariable long id) {
        return new ProductRes(productService.getProduct(id));
    }

    @GetMapping
    public Collection<ProductRes> getProducts() {
        return productService.getProducts()
                .stream()
                .map(ProductRes::new)
                .collect(Collectors.toList());
    }

}
