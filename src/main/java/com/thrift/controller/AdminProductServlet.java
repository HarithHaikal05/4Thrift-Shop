package com.thrift.controller;

import com.thrift.dao.ProductDAO;
import com.thrift.model.Product;
import java.io.*;
import javax.servlet.*; 
import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebServlet("/AdminProductServlet")
@MultipartConfig(maxFileSize = 1024 * 1024 * 10) // 10MB limit
public class AdminProductServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // 1. Capture text fields
            String name = request.getParameter("name");
            String desc = request.getParameter("description");
            double price = Double.parseDouble(request.getParameter("price"));
            String size = request.getParameter("size");
            String category = request.getParameter("category");
            int stock = Integer.parseInt(request.getParameter("stock"));

            // 2. Handle Image Upload
            Part filePart = request.getPart("image");
            String fileName = filePart.getSubmittedFileName();
            // Saves to webapp/assets/products
            String path = getServletContext().getRealPath("") + File.separator + "assets" + File.separator + "products";
            
            File folder = new File(path);
            if (!folder.exists()) folder.mkdirs();
            filePart.write(path + File.separator + fileName);

            // 3. Create Object using your specific constructor
            Product p = new Product(name, desc, price, size, category, fileName, stock);

            // 4. Save via DAO
            ProductDAO dao = new ProductDAO();
            boolean success = dao.addProduct(p);

            // 5. Redirect with status
            if(success) {
                response.sendRedirect("frontend/html/admindashboard.html?msg=success");
            } else {
                response.sendRedirect("frontend/html/admindashboard.html?msg=error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("frontend/html/admindashboard.html?msg=error");
        }
    }
}