package de.ait_tr.g_40_shop.domain.dto;

import java.util.Objects;


public class ProductSupplyDto {

    private Long id;
    private String title;
    private int quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductSupplyDto that)) return false;
        return getQuantity() == that.getQuantity() && Objects.equals(getId(), that.getId()) && Objects.equals(getTitle(), that.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getQuantity());
    }

    @Override
    public String toString() {
        return String.format("Product: id - %d, title - %s, quantity -%d",
                id, title, quantity);
    }
}
