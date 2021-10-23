package com.example.qrmenuapp.Model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Product implements Serializable {

    private String name;
    private String description;
    private Float price;
    private String image;
    private String key;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public Product(String name, String description, Float price, String image, String id) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
    }

}