package com.pizzaapp.basket;

import android.os.Parcel;
import android.os.Parcelable;

public class BasketItem implements Parcelable {
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

    protected BasketItem(Parcel in) {
        id = in.readString();
        productId = in.readString();
        image = in.readString();
        name = in.readString();
        proportion = in.readString();
        price = in.readDouble();
        count = in.readInt();
    }

    public static final Creator<BasketItem> CREATOR = new Creator<BasketItem>() {
        @Override
        public BasketItem createFromParcel(Parcel in) {
            return new BasketItem(in);
        }

        @Override
        public BasketItem[] newArray(int size) {
            return new BasketItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(productId);
        dest.writeString(image);
        dest.writeString(name);
        dest.writeString(proportion);
        dest.writeDouble(price);
        dest.writeInt(count);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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
}