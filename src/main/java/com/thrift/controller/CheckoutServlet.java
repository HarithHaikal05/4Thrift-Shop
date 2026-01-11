package com.thrift.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.thrift.dao.ProductDAO;
import com.thrift.model.Product;
import com.thrift.model.User;
import com.thrift.utils.DBConnection;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        
        // 1. Get User and Cart from Session
        User user = (User) session.getAttribute("currentUser");
        @SuppressWarnings("unchecked")
        ArrayList<Product> cart = (ArrayList<Product>) session.getAttribute("cart");

        // Safety checks
        if (cart == null || cart.isEmpty()) {
            response.sendRedirect("frontend/html/cartpage.html");
            return;
        }
        
        // If user is not logged in properly, force login
        if (user == null) {
            response.sendRedirect("frontend/html/signupLoginpage.html");
            return; 
        }

        // Calculate Total
        double total = 0;
        for (Product p : cart) {
            total += p.getPrice();
        }

        try (Connection con = DBConnection.getConnection()) {
            // 2. INSERT INTO 'orders' TABLE
            // We ask the DB to return the generated ID so we can use it next
            String sqlOrder = "INSERT INTO orders (user_id, total_amount, status) VALUES (?, ?, 'Paid')";
            PreparedStatement psOrder = con.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
            psOrder.setInt(1, user.getId());
            psOrder.setDouble(2, total);
            psOrder.executeUpdate();

            // Get the new Order ID (e.g., Order #5)
            ResultSet rs = psOrder.getGeneratedKeys();
            int newOrderId = 0;
            if (rs.next()) {
                newOrderId = rs.getInt(1);
            }

            // 3. INSERT INTO 'order_items' TABLE
            // We save the product name and price *at time of purchase* String sqlItem = "INSERT INTO order_items (order_id, product_id, product_name, size, quantity, price_at_purchase) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement psItem = con.prepareStatement(sqlItem);

            ProductDAO dao = new ProductDAO();

            for (Product p : cart) {
                psItem.setInt(1, newOrderId);
                psItem.setInt(2, p.getId());
                psItem.setString(3, p.getName());
                psItem.setString(4, p.getSize()); // Saves "S", "M", etc.
                psItem.setInt(5, 1); // Quantity is 1 for now
                psItem.setDouble(6, p.getPrice());
                
                // Add to batch for faster processing
                psItem.addBatch();

                // 4. DECREMENT STOCK
                dao.decrementStock(p.getId());
            }

            // Execute all inserts
            psItem.executeBatch();

            // 5. SUCCESS: Clear cart
            session.removeAttribute("cart");
            
            // Redirect to home
            response.sendRedirect("frontend/html/homepage.html?msg=orderPlaced");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("frontend/html/cartpage.html?error=failed");
        }
    }
}