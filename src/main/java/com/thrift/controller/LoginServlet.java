package com.thrift.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.thrift.model.User;
import com.thrift.utils.DBConnection;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("role"));

                HttpSession session = request.getSession();
                session.setAttribute("currentUser", user);

                response.sendRedirect("frontend/html/homepage.html");
            } else {
                response.sendRedirect("frontend/html/signupLoginpage.html?error=invalid");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("frontend/html/signupLoginpage.html?error=server");
        }
    }
}
