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
        User user = (User) session.getAttribute("currentUser");
        ArrayList<Product> cart = (ArrayList<Product>) session.getAttribute("cart");

        // 1. Capture Form Data
        String fullname = request.getParameter("fullname");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        String payment = request.getParameter("payment");
        
        String fullAddress = fullname + ", " + address + ", " + phone;

        // Validation
        if (cart == null || cart.isEmpty()) {
            response.sendRedirect("frontend/html/cartpage.html");
            return;
        }
        
        if (user == null || user.getId() == 0) {
            // Log this error to console
            System.out.println("Checkout Error: User is null or ID is 0. Session might be stale.");
            response.sendRedirect("frontend/html/signupLoginpage.html");
            return; 
        }

        double total = 0;
        for (Product p : cart) total += p.getPrice();

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false); // Start Transaction

            try {
                // 2. INSERT ORDER
                // UPDATED SQL: Added 'payment_status' to match your database screenshot
                String sqlOrder = "INSERT INTO orders (user_id, total_amount, status, address, payment_method, payment_status) VALUES (?, ?, 'Paid', ?, ?, 'Success')";
                
                PreparedStatement psOrder = con.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
                psOrder.setInt(1, user.getId());
                psOrder.setDouble(2, total);
                psOrder.setString(3, fullAddress);
                psOrder.setString(4, payment);
                // The DB likely expects a value here, or it crashes
                
                psOrder.executeUpdate();

                ResultSet rs = psOrder.getGeneratedKeys();
                int newOrderId = 0;
                if (rs.next()) newOrderId = rs.getInt(1);

                // 3. INSERT ORDER ITEMS
                String sqlItem = "INSERT INTO order_items (order_id, product_id, product_name, size, quantity, price_at_purchase) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement psItem = con.prepareStatement(sqlItem);
                ProductDAO dao = new ProductDAO();

                for (Product p : cart) {
                    psItem.setInt(1, newOrderId);
                    psItem.setInt(2, p.getId());
                    psItem.setString(3, p.getName());
                    
                    // Fallback if size is missing
                    String sizeVal = (p.getSize() == null || p.getSize().equals("undefined")) ? "One Size" : p.getSize();
                    psItem.setString(4, sizeVal);
                    
                    psItem.setInt(5, 1);
                    psItem.setDouble(6, p.getPrice());
                    psItem.addBatch();

                    dao.decrementStock(p.getId());
                }
                psItem.executeBatch();
                
                con.commit(); // Commit Transaction

                // 4. CLEAR CART
                session.removeAttribute("cart");
                
                System.out.println("Checkout Successful. Order ID: " + newOrderId);
                response.sendRedirect("frontend/html/profilepage.html?msg=orderPlaced");

            } catch (Exception ex) {
                con.rollback(); // Undo everything if error
                System.out.println("Checkout Transaction Failed:");
                ex.printStackTrace(); // <--- LOOK AT THIS IN YOUR CONSOLE
                response.sendRedirect("frontend/html/cartpage.html?error=dbError");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("frontend/html/cartpage.html?error=connError");
        }
    }
}