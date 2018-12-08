package com.github.pnowy.store.product;

import com.github.pnowy.store.product.domain.Product;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

//@DataJpaTest
@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaProductTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    public void shouldCreateNewProduct() {
        final Product product = new Product();
        product.setName("Hard drive");
        product.setPrice(new BigDecimal(45.67));
        final Product savedProduct = productRepository.saveAndFlush(product);

//        final Object result = AuditReaderFactory.get(entityManager)
//                .createQuery()
//                .forRevisionsOfEntity(Product.class, false)
//                .addProjection(AuditEntity.revisionNumber().max())
//                .add(AuditEntity.id().eq(savedProduct.getId()))
//                .getSingleResult();
//        System.out.println(result);

//        System.out.println(AuditReaderFactory.get(entityManager).getRevisions(Product.class, savedProduct.getId()));
    }
}
