package com.thrift.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.thrift.utils.DBConnection;
import com.thrift.model.Product;

@WebServlet("/ProductListServlet")
public class ProductListServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        List<Product> productList = new ArrayList<>();
        
        try {
            Connection con = DBConnection.getConnection();
            
            // Get all available products
            String sql = "SELECT * FROM products";
            
            // OPTIONAL: Member 2 can add search features later like:
            // String category = request.getParameter("category");
            // if(category != null) sql += " WHERE category = '" + category + "'";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
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
                
                productList.add(p);
            }
            
            // Send the list to the JSP
            request.setAttribute("products", productList);
            
            // Forward to the Home Page (index.jsp)
            request.getRequestDispatcher("index.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}