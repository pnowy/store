package com.github.pnowy.store.product.domain;

import org.javers.core.JaversBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Rollback(false)
class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository, JaversBuilder.javers().build());
    }

    @Test
    public void shouldCreateNewProduct() {
        // given
        final NewProductCommand newProductCommand = new NewProductCommand("Hard drive", BigDecimal.ONE);

        // when
        final Product hardDrive = productService.createProduct(newProductCommand);

        // then
        assertThat(productService.getProducts()).hasSize(1);
        final Product revisionProduct = productService.getProduct(hardDrive.getRevision());
        assertThat(revisionProduct).isNotNull();
        assertThat(revisionProduct.getPrice()).isEqualTo(hardDrive.getPrice());
    }

    @Test
    void shouldUpdateProduct() {
        // setup
        final Product hdd = productService.createProduct(new NewProductCommand("HDD", BigDecimal.ONE));
        final String hddRevision = hdd.getRevision();

        // given
        final ModifyProductCommand modifyCommand = new ModifyProductCommand(hdd.getId(), "SSD", hdd.getPrice().add(BigDecimal.TEN));

        //when
        final Product ssdDrive = productService.updateProduct(modifyCommand);

        // then
        assertThat(ssdDrive.getRevision()).isNotEqualTo(hddRevision);
        assertThat(productService.getRevisions(ssdDrive.getId())).hasSize(2);
    }


}
