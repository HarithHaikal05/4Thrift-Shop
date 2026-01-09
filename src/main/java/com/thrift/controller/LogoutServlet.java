package com.thrift.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession(false); // Fetch session if it exists
        
        if (session != null) {
            session.invalidate(); // Kill the session
        }
        
        // Redirect back to login page
        response.sendRedirect("login.jsp?msg=Logged out successfully");
    }
}