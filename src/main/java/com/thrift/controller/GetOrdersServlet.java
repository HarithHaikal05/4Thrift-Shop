package com.thrift.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.thrift.model.User;
import com.thrift.utils.DBConnection;

@WebServlet("/GetOrdersServlet")
public class GetOrdersServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            return;
        }

        PrintWriter out = response.getWriter();
        StringBuilder json = new StringBuilder();
        json.append("[");

        try (Connection con = DBConnection.getConnection()) {
            // Get orders for this specific user, newest first
            String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();

            boolean first = true;
            while (rs.next()) {
                if (!first) json.append(",");
                
                json.append("{");
                json.append("\"id\":").append(rs.getInt("id")).append(",");
                json.append("\"date\":\"").append(rs.getTimestamp("order_date").toString()).append("\",");
                json.append("\"status\":\"").append(rs.getString("status")).append("\",");
                json.append("\"total\":").append(rs.getDouble("total_amount"));
                json.append("}");
                
                first = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        json.append("]");
        out.print(json.toString());
    }
}