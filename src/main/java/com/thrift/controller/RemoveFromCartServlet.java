package com.thrift.controller;

import com.thrift.model.Product;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/RemoveFromCartServlet")
public class RemoveFromCartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get ID and Size to identify what to remove
        int productId = Integer.parseInt(request.getParameter("productId"));
        String size = request.getParameter("size");

        HttpSession session = request.getSession();
        ArrayList<Product> cart = (ArrayList<Product>) session.getAttribute("cart");

        if (cart != null) {
            // Use Iterator to safely remove while looping
            Iterator<Product> iterator = cart.iterator();
            while (iterator.hasNext()) {
                Product p = iterator.next();
                // Find first match based on ID AND Size
                if (p.getId() == productId && p.getSize().equals(size)) {
                    iterator.remove();
                    break; // Remove only one instance, then stop
                }
            }
            session.setAttribute("cart", cart);
        }

        // Send back success status so JS can reload the cart
        response.setStatus(HttpServletResponse.SC_OK);
    }
}