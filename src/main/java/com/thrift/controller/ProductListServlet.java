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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        ProductDAO dao = new ProductDAO();
        List<Product> products = dao.getAllProducts();
        
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
            json.append("\"stock\":").append(p.getStock()).append(","); // <--- ADDED STOCK HERE
            json.append("\"image\":\"").append(clean(p.getImageUrl())).append("\"");
            json.append("}");
            
            if (i < products.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        out.print(json.toString());
    }
    
    private String clean(String s) {
        return s == null ? "" : s.replace("\"", "\\\"").replace("\n", " ");
    }
}