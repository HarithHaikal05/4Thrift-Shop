package com.thrift.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.thrift.utils.DBConnection;

@WebServlet("/ProductListServlet")
public class ProductListServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. Tell the browser we are sending JSON text, not HTML
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM products";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            // 2. Manually build the JSON String (Since we can't use libraries like Gson)
            StringBuilder json = new StringBuilder("[");
            boolean first = true;
            
            while (rs.next()) {
                if (!first) json.append(",");
                first = false;
                
                json.append("{");
                json.append("\"id\":").append(rs.getInt("id")).append(",");
                json.append("\"name\":\"").append(rs.getString("name")).append("\",");
                json.append("\"price\":").append(rs.getDouble("price")).append(",");
                json.append("\"image\":\"").append(rs.getString("image_url")).append("\"");
                json.append("}");
            }
            json.append("]");
            
            // 3. Send the text to the browser
            out.print(json.toString());
            out.flush();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
