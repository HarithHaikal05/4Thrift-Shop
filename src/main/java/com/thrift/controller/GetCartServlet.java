package com.thrift.controller;

import com.thrift.model.Product;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/GetCartServlet")
public class GetCartServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Setup JSON response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 2. Get the cart from the session
        HttpSession session = request.getSession();
        ArrayList<Product> cart = (ArrayList<Product>) session.getAttribute("cart");

        PrintWriter out = response.getWriter();
        StringBuilder json = new StringBuilder();
        
        // 3. Build JSON manually
        json.append("[");
        if (cart != null && !cart.isEmpty()) {
            for (int i = 0; i < cart.size(); i++) {
                Product p = cart.get(i);
                json.append("{");
                json.append("\"id\":").append(p.getId()).append(",");
                json.append("\"name\":\"").append(clean(p.getName())).append("\",");
                json.append("\"price\":").append(p.getPrice()).append(",");
                // We need the image to show it in the cart!
                json.append("\"image\":\"").append(clean(p.getImageUrl())).append("\""); 
                json.append("}");
                
                if (i < cart.size() - 1) json.append(",");
            }
        }
        json.append("]");
        out.print(json.toString());
    }

    private String clean(String s) {
        return s == null ? "" : s.replace("\"", "\\\"").replace("\n", " ");
    }
}