package com.thrift.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.thrift.utils.DBConnection;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. Get data from the HTML form (Member 3 will build register.jsp)
        String uName = request.getParameter("username");
        String pass = request.getParameter("password");
        String email = request.getParameter("email");
        
        // Default role is "customer"
        String role = "customer";

        try {
            // 2. Connect to Database
            Connection con = DBConnection.getConnection();
            
            // 3. Prepare the SQL INSERT
            String sql = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.setString(1, uName);
            ps.setString(2, pass);
            ps.setString(3, email);
            ps.setString(4, role);
            
            // 4. Execute the update
            int rows = ps.executeUpdate();
            
            if (rows > 0) {
                // Success: Send them to Login Page
                response.sendRedirect("login.jsp?msg=Registered Successfully!");
            } else {
                // Fail
                response.sendRedirect("register.jsp?error=Could not register");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("register.jsp?error=Database Error: " + e.getMessage());
        }
    }
}