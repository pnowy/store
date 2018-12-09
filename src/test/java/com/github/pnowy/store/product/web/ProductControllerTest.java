package com.github.pnowy.store.product.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pnowy.store.product.domain.Product;
import com.github.pnowy.store.product.domain.ProductService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "HDD", BigDecimal.TEN);
        when(this.productService.createProduct(any())).thenReturn(product);
        when(this.productService.updateProduct(any())).thenReturn(product);
        when(this.productService.getProduct(1L)).thenReturn(product);
        when(this.productService.getProduct(33L)).thenThrow(new EntityNotFoundException());
    }

    @Test
    void shouldValidateAndSaveProduct() throws Exception {
        final ProductReq productReq = new ProductReq(product.getName(), product.getPrice());

        this.mvc.perform(post("/api/product").content(asJsonString(productReq)).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(product.getId().intValue())))
                .andExpect(jsonPath("$.price", Matchers.notNullValue()))
                .andExpect(jsonPath("$.name", Matchers.notNullValue()))
                .andDo(print())
        ;
    }

    @Test
    void getValidateAndUpdateProduct() throws Exception {
        final ProductReq productReq = new ProductReq(product.getName(), product.getPrice());

        this.mvc.perform(post("/api/product").content(asJsonString(productReq)).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(product.getId().intValue())))
                .andExpect(jsonPath("$.price", Matchers.notNullValue()))
                .andExpect(jsonPath("$.name", Matchers.notNullValue()))
                .andDo(print())
        ;
    }

    @Test
    void shouldNotCreateProductWithoutProvidedName() throws Exception {
        final ProductReq productReq = new ProductReq(null, product.getPrice());

        this.mvc.perform(post("/api/product").content(asJsonString(productReq)).contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
        ;
    }

    @Test
    void shouldGetNotFoundExForNonExistedProduct() throws Exception {
        final ProductReq productReq = new ProductReq("SSD", product.getPrice());

        this.mvc.perform(get("/api/product/33").content(asJsonString(productReq)).contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
        ;
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

}
