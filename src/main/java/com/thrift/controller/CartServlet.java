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

        String idParam = request.getParameter("productId");
        String selectedSize = request.getParameter("selectedSize"); // <--- CAPTURE SIZE

        if(idParam == null) {
            response.sendRedirect("frontend/html/homepage.html");
            return;
        }
        int productId = Integer.parseInt(idParam);

        HttpSession session = request.getSession();
        ArrayList<Product> cart = (ArrayList<Product>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();

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
                p.setImageUrl(rs.getString("image_url"));
                
                // IMPORTANT: We overwrite the DB size with the User's choice
                if (selectedSize != null && !selectedSize.isEmpty()) {
                    p.setSize(selectedSize); 
                } else {
                    p.setSize(rs.getString("size")); // Fallback
                }
                
                cart.add(p);
            }
            session.setAttribute("cart", cart);
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("frontend/html/cartpage.html");
    }
}