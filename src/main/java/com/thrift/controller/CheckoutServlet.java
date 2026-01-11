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
        // IMPORTANT: You must log out and log back in so the session has the User ID.
        if (user == null || user.getId() == 0) {
            response.sendRedirect("frontend/html/signupLoginpage.html");
            return; 
        }

        // Calculate Total
        double total = 0;
        for (Product p : cart) {
            total += p.getPrice();
        }

        try (Connection con = DBConnection.getConnection()) {
            // Turn off auto-commit for transaction safety
            con.setAutoCommit(false);

            try {
                // 2. INSERT INTO 'orders' TABLE
                String sqlOrder = "INSERT INTO orders (user_id, total_amount, status) VALUES (?, ?, 'Paid')";
                PreparedStatement psOrder = con.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
                psOrder.setInt(1, user.getId());
                psOrder.setDouble(2, total);
                psOrder.executeUpdate();

                // Get the new Order ID
                ResultSet rs = psOrder.getGeneratedKeys();
                int newOrderId = 0;
                if (rs.next()) {
                    newOrderId = rs.getInt(1);
                }

                // 3. INSERT INTO 'order_items' TABLE
                // FIXED SYNTAX ERROR HERE
                String sqlItem = "INSERT INTO order_items (order_id, product_id, product_name, size, quantity, price_at_purchase) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement psItem = con.prepareStatement(sqlItem);

                ProductDAO dao = new ProductDAO();

                for (Product p : cart) {
                    psItem.setInt(1, newOrderId);
                    psItem.setInt(2, p.getId());
                    psItem.setString(3, p.getName());
                    psItem.setString(4, p.getSize());
                    psItem.setInt(5, 1); // Quantity is 1 per row in the session list
                    psItem.setDouble(6, p.getPrice());
                    
                    psItem.addBatch();

                    // 4. DECREMENT STOCK
                    dao.decrementStock(p.getId());
                }

                // Execute all inserts
                psItem.executeBatch();
                
                // Commit transaction
                con.commit();

                // 5. SUCCESS: Clear cart
                session.removeAttribute("cart");
                response.sendRedirect("frontend/html/homepage.html?msg=orderPlaced");

            } catch (Exception ex) {
                con.rollback(); // Undo changes if something went wrong
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Redirect with error message so you know it failed
            response.sendRedirect("frontend/html/cartpage.html?error=checkoutFailed");
        }
    }
}