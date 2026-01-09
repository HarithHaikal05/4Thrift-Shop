package com.thrift.model;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private String size;
    private String category;
    private String imageUrl;
    private int stock;

    public Product() {}

    public Product(String name, String description, double price, String size, String category, String imageUrl, int stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.size = size;
        this.category = category;
        this.imageUrl = imageUrl;
        this.stock = stock;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}