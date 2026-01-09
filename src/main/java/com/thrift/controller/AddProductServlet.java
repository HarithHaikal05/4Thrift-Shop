package com.thrift.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import com.thrift.utils.DBConnection;

@WebServlet("/AddProductServlet")
@MultipartConfig( // Essential for file uploads!
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class AddProductServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try {
            // 1. Get Text Data
            String name = request.getParameter("name");
            String desc = request.getParameter("description");
            double price = Double.parseDouble(request.getParameter("price"));
            String size = request.getParameter("size");
            String category = request.getParameter("category");
            int stock = Integer.parseInt(request.getParameter("stock"));

            // 2. Handle Image Upload
            Part filePart = request.getPart("image"); // Matches html <input type="file" name="image">
            String fileName = filePart.getSubmittedFileName();
            
            // Define where to save the image. 
            // For this project, we save to the "images" folder inside the server
            String savePath = getServletContext().getRealPath("") + File.separator + "images";
            File fileSaveDir = new File(savePath);
            if (!fileSaveDir.exists()) {
                fileSaveDir.mkdir(); // Create folder if missing
            }
            
            // Save the file
            String finalPath = savePath + File.separator + fileName;
            filePart.write(finalPath);

            // 3. Save info to Database
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO products (name, description, price, size, category, stock, image_url) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.setString(1, name);
            ps.setString(2, desc);
            ps.setDouble(3, price);
            ps.setString(4, size);
            ps.setString(5, category);
            ps.setInt(6, stock);
            ps.setString(7, "images/" + fileName); // Save the RELATIVE path to DB

            ps.executeUpdate();
            
            // 4. Success
            response.sendRedirect("admin_dashboard.jsp?msg=Product Added Successfully");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("add_product.jsp?error=Error saving product: " + e.getMessage());
        }
    }
}