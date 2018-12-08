package com.github.pnowy.store.product.domain;

import com.github.pnowy.store.common.AppPreconditions;
import com.google.common.collect.ImmutableMap;
import org.javers.core.Javers;
import org.javers.repository.jql.JqlQuery;
import org.javers.repository.jql.QueryBuilder;
import org.javers.shadow.Shadow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private static final String AUTHOR = "system";
    private static final String REVISION_PROPERTY = "revision";

    private final ProductRepository productRepository;
    private final Javers javers;

    ProductService(ProductRepository productRepository, Javers javers) {
        this.productRepository = productRepository;
        this.javers = javers;
    }

    @Transactional
    public Product createProduct(NewProductCommand command) {
        final Product product = new Product();
        command.applyState(product);
        final Product savedProduct = productRepository.save(product);
        javers.commit(AUTHOR, savedProduct, ImmutableMap.of(REVISION_PROPERTY, savedProduct.getRevision()));
        log.info("Product creation. Product id={}, revision id={}", savedProduct.getId(), savedProduct.getRevision());
        return savedProduct;
    }

    @Transactional
    public Product updateProduct(ModifyProductCommand command) {
        final Product product = getProduct(command.getId());
        command.applyState(product);
        product.applyNewRevision();
        final Product savedProduct = productRepository.save(product);
        javers.commit(AUTHOR, savedProduct, ImmutableMap.of(REVISION_PROPERTY, savedProduct.getRevision()));
        log.info("Product update. Product id={}, revision id={}", savedProduct.getId(), savedProduct.getRevision());
        return savedProduct;
    }

    @Transactional(readOnly = true)
    public Product getProduct(long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("The product with id %s has not been found!", id)));
    }

    @Transactional(readOnly = true)
    public Collection<Product> getProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product getProduct(String revision) {
        final JqlQuery byRevision = QueryBuilder
                .byClass(Product.class)
                .withCommitProperty(REVISION_PROPERTY, revision)
                .build();

        final List<Shadow<Product>> shadows = javers.findShadows(byRevision);
        AppPreconditions.check(!shadows.isEmpty(), () -> new EntityNotFoundException(String.format("The production with revision %s doesn't exist", revision)));
        AppPreconditions.check(shadows.size() == 1, () -> new IllegalStateException(String.format("Duplicated product revision id: %s. Unexpected situation.", revision)));

        return shadows.iterator().next().get();
    }

    @Transactional(readOnly = true)
    public Collection<Product> getRevisions(long id) {
        final JqlQuery byInstanceId = QueryBuilder
                .byInstanceId(id, Product.class)
                .build();
        final Stream<Shadow<Product>> shadowStream = javers.findShadowsAndStream(byInstanceId);
        return shadowStream
                .map(Shadow::get)
                .collect(Collectors.toList());
    }

}
