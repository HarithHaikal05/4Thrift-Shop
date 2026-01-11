package com.thrift.controller;

import com.thrift.utils.DBConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ProductDetailServlet")
public class ProductDetailServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String idParam = request.getParameter("id");
        if (idParam == null) return;

        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM products WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(idParam));
            ResultSet rs = ps.executeQuery();

            PrintWriter out = response.getWriter();
            if (rs.next()) {
                StringBuilder json = new StringBuilder();
                json.append("{");
                json.append("\"id\":").append(rs.getInt("id")).append(",");
                json.append("\"name\":\"").append(clean(rs.getString("name"))).append("\",");
                json.append("\"description\":\"").append(clean(rs.getString("description"))).append("\",");
                json.append("\"price\":").append(rs.getDouble("price")).append(",");
                json.append("\"stock\":").append(rs.getInt("stock")).append(","); // <--- ADDED STOCK HERE
                json.append("\"size\":\"").append(clean(rs.getString("size"))).append("\",");
                json.append("\"category\":\"").append(clean(rs.getString("category"))).append("\",");
                json.append("\"image\":\"").append(clean(rs.getString("image_url"))).append("\"");
                json.append("}");
                out.print(json.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String clean(String s) {
        return s == null ? "" : s.replace("\"", "\\\"").replace("\n", " ");
    }
}