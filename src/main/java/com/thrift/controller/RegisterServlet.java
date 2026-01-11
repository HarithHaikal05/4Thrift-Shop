    package com.thrift.controller;

    import java.io.IOException;
    import java.sql.Connection;
    import java.sql.PreparedStatement;

    import javax.servlet.ServletException;
    import javax.servlet.annotation.WebServlet;
    import javax.servlet.http.*;

    import com.thrift.utils.DBConnection;

    @WebServlet("/RegisterServlet")
    public class RegisterServlet extends HttpServlet {

        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String email = request.getParameter("email");

            try {
                Connection con = DBConnection.getConnection();
                String sql = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)";

                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, email);
                ps.setString(4, "customer");

                ps.executeUpdate();

                response.sendRedirect("frontend/html/signupLoginpage.html?msg=registered");

            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("frontend/html/signupLoginpage.html?error=server");
            }
        }
    }
