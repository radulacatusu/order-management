package com.mine.ordermgm.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mine.ordermgm.util.BigDecimalSerializer;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

/**
 * @stefanl
 */
@Entity
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "The database generated order ID")
    private long id;

    @NotBlank
    @ApiModelProperty(notes = "The email address of the order")
    private String email;

    @NonNull
    @Column(name = "ORDER_DATE")
    @ApiModelProperty(notes = "The order date")
    private Timestamp orderDate;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @Column(name = "ORDER_AMOUNT", scale = 2, precision = 15)
    @ApiModelProperty(notes = "The total order amount")
    private BigDecimal orderAmount;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "order_product", joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"))
    @ApiModelProperty(notes = "The products of the order")
    private Set<Product> products;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(email, order.email) &&
                Objects.equals(orderAmount, order.orderAmount) &&
                Objects.equals(products, order.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, orderDate, orderAmount, products);
    }
}
