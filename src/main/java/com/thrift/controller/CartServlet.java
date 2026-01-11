package com.thrift.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.thrift.model.Product;
import com.thrift.utils.DBConnection;

@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Get Product ID from the form
        String idParam = request.getParameter("productId");
        if(idParam == null) {
            response.sendRedirect("frontend/html/homepage.html");
            return;
        }
        int productId = Integer.parseInt(idParam);

        // 2. Get or Create Cart in Session
        HttpSession session = request.getSession();
        ArrayList<Product> cart = (ArrayList<Product>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();

        // 3. Look up Product in DB
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM products WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getDouble("price"));
                p.setImageUrl(rs.getString("image_url")); // <--- CRITICAL: Save image
                p.setSize(rs.getString("size"));
                
                cart.add(p);
            }
            session.setAttribute("cart", cart);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 4. Redirect to Cart Page
        response.sendRedirect("frontend/html/cartpage.html");
    }
}