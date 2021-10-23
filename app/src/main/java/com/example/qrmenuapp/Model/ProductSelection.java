package com.example.qrmenuapp.Model;

import java.io.Serializable;
import java.util.UUID;

public class ProductSelection implements Serializable {
    public final Product product;
    public Integer quantity;
    public Float totalPrice;
    public final String id;

    public Product getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getId() {
        return id;
    }

    public ProductSelection(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = product.getPrice() * quantity;
        this.id = UUID.randomUUID().toString();
    }

    private Float computeTotalPrice() {
        return this.totalPrice = product.getPrice() * this.quantity;
    }

    public void increaseQuantity() {
        this.quantity += 1;
        computeTotalPrice();
    }

    public void decreaseQuantity() {
        if(negativeQuantity() == true) {
            return;
        } else {
            this.quantity -= 1;
            computeTotalPrice();
        }
    }

    private boolean negativeQuantity() {
        if(this.quantity <= 1) {
            return true;
        }
        return false;
    }
}
