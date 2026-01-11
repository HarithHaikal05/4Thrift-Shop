package com.thrift.controller;

import com.thrift.dao.ProductDAO;
import com.thrift.model.Product;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ProductListServlet")
public class ProductListServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Prepare response format
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // 2. Get data using the DAO
        ProductDAO dao = new ProductDAO();
        List<Product> products = dao.getAllProducts();
        
        // 3. Build JSON String manually (Array of Objects)
        PrintWriter out = response.getWriter();
        StringBuilder json = new StringBuilder();
        
        json.append("[");
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            
            json.append("{");
            json.append("\"id\":").append(p.getId()).append(",");
            json.append("\"name\":\"").append(clean(p.getName())).append("\",");
            json.append("\"category\":\"").append(clean(p.getCategory())).append("\",");
            json.append("\"size\":\"").append(clean(p.getSize())).append("\",");
            json.append("\"price\":").append(p.getPrice()).append(",");
            json.append("\"image\":\"").append(clean(p.getImageUrl())).append("\"");
            json.append("}");
            
            if (i < products.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        
        // 4. Send back to Javascript
        out.print(json.toString());
        out.flush();
    }
    
    // Helper to prevent JSON syntax errors if strings have quotes
    private String clean(String s) {
        if (s == null) return "";
        return s.replace("\"", "\\\"").replace("\n", " ");
    }
}