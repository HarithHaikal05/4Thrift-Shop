package com.thrift.dao;

import com.thrift.model.Product;
import com.thrift.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // 1. ADD PRODUCT
    public boolean addProduct(Product p) {
        String sql = "INSERT INTO products (name, description, price, size, category, image_url, stock) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getPrice());
            ps.setString(4, p.getSize());
            ps.setString(5, p.getCategory());
            ps.setString(6, p.getImageUrl());
            ps.setInt(7, p.getStock());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. GET ALL PRODUCTS
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products ORDER BY id DESC"; 

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getDouble("price"));
                p.setSize(rs.getString("size"));
                p.setCategory(rs.getString("category"));
                p.setImageUrl(rs.getString("image_url"));
                p.setStock(rs.getInt("stock"));
                
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. DECREMENT STOCK (New Method)
    public void decrementStock(int productId) {
        String sql = "UPDATE products SET stock = stock - 1 WHERE id = ? AND stock > 0";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, productId);
            ps.executeUpdate();
            System.out.println("Stock decremented for Product ID: " + productId);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}