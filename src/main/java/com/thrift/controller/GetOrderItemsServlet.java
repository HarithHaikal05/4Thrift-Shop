package com.thrift.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.thrift.utils.DBConnection;

@WebServlet("/GetOrderItemsServlet")
public class GetOrderItemsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String orderIdParam = request.getParameter("orderId");
        if (orderIdParam == null) return;
        int orderId = Integer.parseInt(orderIdParam);

        PrintWriter out = response.getWriter();
        StringBuilder json = new StringBuilder();
        json.append("[");

        try (Connection con = DBConnection.getConnection()) {
            // JOIN query to get the image from the products table
            String sql = "SELECT oi.*, p.image_url " +
                         "FROM order_items oi " +
                         "LEFT JOIN products p ON oi.product_id = p.id " +
                         "WHERE oi.order_id = ?";
                         
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            boolean first = true;
            while (rs.next()) {
                if (!first) json.append(",");
                
                String img = rs.getString("image_url");
                if (img == null) img = ""; // Handle deleted products

                json.append("{");
                json.append("\"name\":\"").append(clean(rs.getString("product_name"))).append("\",");
                json.append("\"size\":\"").append(clean(rs.getString("size"))).append("\",");
                json.append("\"price\":").append(rs.getDouble("price_at_purchase")).append(",");
                json.append("\"qty\":").append(rs.getInt("quantity")).append(",");
                json.append("\"image\":\"").append(clean(img)).append("\"");
                json.append("}");
                
                first = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        json.append("]");
        out.print(json.toString());
    }

    private String clean(String s) {
        return s == null ? "" : s.replace("\"", "\\\"").replace("\n", " ");
    }
}