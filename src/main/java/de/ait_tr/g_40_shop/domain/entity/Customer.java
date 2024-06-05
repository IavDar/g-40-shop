package de.ait_tr.g_40_shop.domain.entity;

import jakarta.persistence.*;

import java.util.Objects;
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "active")
    private boolean active;
//    private Cart cart;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

//    public void setCart(Cart cart) {
//        this.cart = cart;
//    }
//
//    public Cart getCart() {
//        return cart;
//    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Customer customer)) return false;
//        return isActive() == customer.isActive() && Objects.equals(getId(), customer.getId()) && Objects.equals(getName(), customer.getName()) && Objects.equals(getCart(), customer.getCart());
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(getId(), getName(), isActive(), getCart());
//    }
//
//    @Override
//    public String toString() {
//        return String.format("Customer: id - %d, name - %s, active - %s, cart - %s",
//                id, name, active ? "yes" : "no", cart == null ? "ERROR! Cart is missing" : cart);
//    }
}
