package com.example.qrmenuapp.Model;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Cart implements Serializable {

    private String cartId;
    private String tableNumber;
    private Date createdDate;
    private ArrayList<ProductSelection> productSelections;
    private Float totalPrice;
    private boolean ordered;
    private boolean served;

    public Float getTotalPrice() { return totalPrice; }

    public void setTotalPrice(Float totalPrice) { this.totalPrice = totalPrice; }

    public String getCartId() {
        return cartId;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public ArrayList<ProductSelection> getProductSelections() {
        return productSelections;
    }

    public void setProductSelections(ArrayList<ProductSelection> productSelections) {
        this.productSelections = productSelections;
    }

    public boolean isOrdered() { return ordered; }

    public void setOrdered(boolean ordered) { this.ordered = ordered; }

    public boolean isServed() { return served; }

    public void setServed(boolean served) { this.served = served; }

    public Cart() {};

    public Cart(String cartId,String tableNumber) {
        this.cartId = cartId;
        this.tableNumber = tableNumber;
    }

    public Cart(String cartId, String tableNumber, boolean ordered, boolean served) {
        this.cartId = cartId;
        this.tableNumber = tableNumber;
        this.ordered = ordered;
        this.served = served;
    }

    public Cart(String cartId, String tableNumber, ArrayList<ProductSelection> productSelections, boolean ordered, boolean served) {
        this.cartId = cartId;
        this.tableNumber = tableNumber;
        this.productSelections = productSelections;
        this.ordered = ordered;
        this.served = served;
        computeTotalPrice();
    }

    public Cart(String cartId, String tableNumber, Date createdDate, ArrayList<ProductSelection> productSelections, boolean ordered, boolean served) {
        this.cartId = cartId;
        this.tableNumber = tableNumber;
        this.createdDate = createdDate;
        this.productSelections = productSelections;
        this.totalPrice = Float.parseFloat("0");
        this.ordered = ordered;
        this.served = served;
        computeTotalPrice();
    }

    public Cart addSelectionToCart(ProductSelection productSelection) {
        boolean inCart = productInCart(productSelection.getProduct().getId());

        if(inCart == true) {
            Log.d("ps", "u korpi je");
            int index = findIndexOfProduct(productSelection.getProduct().getId());
            Log.d("ps", "index: " + index);
            this.productSelections.get(index).increaseQuantity();

        } else {
            Log.d("ps", "nije u korpi");
            this.productSelections.add(productSelection);
        }
        computeTotalPrice();
        return this;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Cart removeSelectionFromCart(String psId) {
        this.productSelections.removeIf(ps -> psId.equals(ps));
        computeTotalPrice();
        return this;
    }


    public float computeTotalPrice() {
        this.totalPrice = Float.parseFloat("0");
        for(int i = 0; i < productSelections.size(); i++) {
            this.totalPrice += (productSelections.get(i).getProduct().getPrice() * productSelections.get(i).getQuantity());
        }
        return this.totalPrice;
    }

    private boolean productInCart(String productId) {
        for(int i = 0; i < this.productSelections.size(); i++) {
            if(productId != null && productId.equals(this.productSelections.get(i).getProduct().getId())) {
                return true;
            }
        }
        return false;
    }

    private int findIndexOfProduct(String productId) {
        for(int i = 0; i < this.productSelections.size(); i++) {
            if(productId.equals(this.productSelections.get(i).getProduct().getId())) {
                return i;
            }
        }
        return 0;
    }
}
