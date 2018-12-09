package com.github.pnowy.store.product.domain;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;

@Entity
@Table(name = "st_product")
public class Product implements Serializable {

    Product() {
        setNewRevision();
    }

    public Product(Long id, String name, BigDecimal price) {
        this();
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String revision;
    @NotEmpty
    @Column(nullable = false)
    private String name;
    @NotNull
    @Positive
    @Column(nullable = false)
    private BigDecimal price;

    private void setNewRevision() {
        this.revision = String.format("%s_%s", Instant.now().getEpochSecond(), RandomStringUtils.randomAlphanumeric(16));
    }

    void handle(NewProductCommand command) {
        checkArgument(Objects.isNull(getId()), "Cannot apply state for persisted product!");
        setPrice(command.getPrice());
        setName(command.getName());
    }

    void handle(ModifyProductCommand command) {
        checkArgument(Objects.equals(getId(), command.getId()), "Cannot apply state for different product that expected based on command");
        setPrice(command.getPrice());
        setName(command.getName());
        setNewRevision();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRevision() {
        return revision;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
