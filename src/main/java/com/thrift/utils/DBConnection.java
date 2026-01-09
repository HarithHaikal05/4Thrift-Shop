package com.thrift.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // These are the standard XAMPP database settings
    private static final String URL = "jdbc:mysql://localhost:3306/thrift_db";
    private static final String USER = "root";
    private static final String PASS = ""; // Leave blank for default XAMPP

    public static Connection getConnection() {
        Connection con = null;
        try {
            // This loads the MySQL driver you just added to the lib folder
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            con = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Database connected successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: MySQL Driver not found. Did you add the JAR to lib?");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error: Could not connect to DB. Is XAMPP/MySQL running?");
            e.printStackTrace();
        }
        return con;
    }
}