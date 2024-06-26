package com.pizzaapp.products;

import java.util.List;

public class Product {
    private String id;
    private String name;
    private List<Double> price;
    private List<Double> weight;
    private List<String> images;

    public Product() {}

    public Product(String id, String name, List<Double> price, List<Double> weight, List<String> images) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.images = images;
    }

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

    public List<Double> getPrice() {
        return price;
    }

    public void setPrice(List<Double> price) {
        this.price = price;
    }

    public List<Double> getWeight() {
        return weight;
    }

    public void setWeight(List<Double> weight) {
        this.weight = weight;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
