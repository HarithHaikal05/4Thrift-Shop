package com.thrift.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.thrift.utils.DBConnection;
import com.thrift.model.User;

@WebServlet("/LoginServlet") // This means the HTML form will post to "LoginServlet"
public class LoginServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. Get data from the HTML form (Member 3 will build the form)
        String uName = request.getParameter("username");
        String pass = request.getParameter("password");

        try {
            // 2. Connect to Database
            Connection con = DBConnection.getConnection();
            
            // 3. Check if user exists
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, uName);
            ps.setString(2, pass);
            
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // 4. User found! Create a Session
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("role")); // Important: Admin vs Customer
                
                HttpSession session = request.getSession();
                session.setAttribute("currentUser", user); // Save user in "memory"

                // 5. Redirect based on Role
                if ("admin".equals(user.getRole())) {
                    response.sendRedirect("admin_dashboard.jsp"); // Member 4 will build this
                } else {
                    response.sendRedirect("index.jsp"); // Member 2 is building this
                }
            } else {
                // 6. Login Failed
                response.sendRedirect("login.jsp?error=Invalid Credentials");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login.jsp?error=Server Error");
        }
    }
}