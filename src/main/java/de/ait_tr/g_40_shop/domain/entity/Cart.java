package de.ait_tr.g_40_shop.domain.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

//@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private Customer customer;
    private List<Product> products;

    public void addProduct(Product product) {
        if (product.isActive()) {
            products.add(product);
        }
    }

    public List<Product> getAllActiveProducts() {
        return products;
    }

    public void removeById(Long productId) {
        products.removeIf(product -> Objects.equals(product.getId(), productId));

    }

    public void removeAllProducts() {
        products.clear();
    }

    public BigDecimal getTotalCost() {
        BigDecimal sum = new BigDecimal(0);

        for (Product product: products) {
            sum = sum.add(product.getPrice());
        }

        return sum;
    }

    public BigDecimal getAverageCost() {
        BigDecimal sum = this.getTotalCost();
        return sum.divide(new BigDecimal((long) products.size()));
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cart cart)) return false;
        return Objects.equals(getId(), cart.getId()) && Objects.equals(getCustomer(), cart.getCustomer()) && Objects.equals(getProducts(), cart.getProducts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCustomer(), getProducts());
    }

    @Override
    public String toString() {
        return String.format("Cart: id - %d, contains %d products",
                id, products == null ? 0 : products.size());
    }
}
