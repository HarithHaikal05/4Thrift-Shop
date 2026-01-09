package com.thrift.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.thrift.model.Product;
import com.thrift.model.User;
import com.thrift.utils.DBConnection;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("currentUser");
        ArrayList<Product> cart =
                (ArrayList<Product>) session.getAttribute("cart");

        double total = 0;
        for (Product p : cart) {
            total += p.getPrice();
        }

        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO orders (user_id, total_amount) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, user.getId());
            ps.setDouble(2, total);

            ps.executeUpdate();

            session.removeAttribute("cart");

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("html/homepage.html?msg=orderPlaced");
    }
}
