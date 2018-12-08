package com.github.pnowy.store.product;

import com.github.pnowy.store.product.domain.ModifyProductCommand;
import com.github.pnowy.store.product.domain.NewProductCommand;
import com.github.pnowy.store.product.domain.Product;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.Collection;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final EntityManager entityManager;

    public ProductService(ProductRepository productRepository, EntityManager entityManager) {
        this.productRepository = productRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public Product createProduct(NewProductCommand command) {
        final Product product = new Product();
        command.applyState(product);
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(ModifyProductCommand command) {
        final Product product = getProduct(command.getId());
        command.applyState(product);
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Product getProduct(long id) {

        final Object result = AuditReaderFactory.get(entityManager)
                .createQuery()
                .forRevisionsOfEntity(Product.class, false)
                .addProjection(AuditEntity.revisionNumber().max())
                .add(AuditEntity.id().eq(id))
                .getSingleResult();
        System.out.println(result);

        return productRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("The product with id %s has not been found!", id)));
    }

    @Transactional(readOnly = true)
    public Collection<Product> getProducts() {
        return productRepository.findAll();
    }
}
