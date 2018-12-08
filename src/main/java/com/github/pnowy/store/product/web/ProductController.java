package com.github.pnowy.store.product.web;

import com.github.pnowy.store.product.ProductService;
import com.github.pnowy.store.product.domain.ModifyProductCommand;
import com.github.pnowy.store.product.domain.NewProductCommand;
import com.github.pnowy.store.product.domain.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

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
    public Product createProduct(@Valid @RequestBody NewProductReq product) {
        final NewProductCommand command = new NewProductCommand(product.getName(), product.getPrice());
        log.info("Create new product: {}", command);
        return productService.createProduct(command);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable long id, @Valid @RequestBody ModifyProductReq product) {
        final ModifyProductCommand modifyCommand = new ModifyProductCommand(id, product.getName(), product.getPrice());
        log.info("Update product: {}", modifyCommand);
        return productService.updateProduct(modifyCommand);
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable long id) {
        return productService.getProduct(id);
    }

    @GetMapping
    public Collection<Product> getProducts() {
        return productService.getProducts();
    }

}
