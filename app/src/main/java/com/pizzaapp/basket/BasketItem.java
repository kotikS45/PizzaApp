package com.pizzaapp.basket;

public class BasketItem {
    private String id;
    private String productId;
    private String image;
    private String name;
    private String proportion;
    private double price;
    private int count;

    public BasketItem() {}

    public BasketItem(String id, String productId, String image, String name, String proportion, double price, int count) {
        this.id = id;
        this.productId = productId;
        this.image = image;
        this.name = name;
        this.proportion = proportion;
        this.price = price;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

    public double getPrice() {
        return price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
