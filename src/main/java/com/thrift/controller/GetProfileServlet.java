package com.thrift.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.thrift.model.User;

@WebServlet("/GetProfileServlet")
public class GetProfileServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");

        if (user != null) {
            // Return User Info as JSON
            PrintWriter out = response.getWriter();
            String json = String.format(
                "{\"username\":\"%s\", \"email\":\"%s\", \"role\":\"%s\"}",
                user.getUsername(), 
                user.getEmail() != null ? user.getEmail() : "No Email",
                user.getRole()
            );
            out.print(json);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}