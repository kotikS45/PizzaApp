package com.pizzaapp.categories;

public class Category {
    private String id;
    private String name;
    private String image;
    private int productsCount;

    public Category() {
    }

    public Category(String id, String name, String image, int productCount) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.productsCount = productCount;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getProductsCount() {
        return productsCount;
    }

    public void setProductsCount(int productsCount) {
        this.productsCount = productsCount;
    }
}
