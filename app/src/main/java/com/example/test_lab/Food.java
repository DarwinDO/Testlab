package com.example.test_lab;

import java.io.Serializable;

/**
 * Model class đại diện cho một món ăn
 * Implements Serializable để có thể truyền qua Intent
 */
public class Food implements Serializable {
    private int id;
    private String name; // Tên món ăn
    private String description; // Mô tả
    private int price; // Đơn giá
    private int imageResId; // Resource ID của hình ảnh

    // Constructor
    public Food(int id, String name, String description, int price, int imageResId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResId = imageResId;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    @Override
    public String toString() {
        return name + " - " + price + " VNĐ";
    }
}
