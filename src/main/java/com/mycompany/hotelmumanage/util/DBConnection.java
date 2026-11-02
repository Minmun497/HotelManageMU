package com.mycompany.hotelmumanage.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/hotelmanagemu", "Minh", "04092007"
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Chạy thử kiểm tra kết nối
    public static void main(String[] args) {
        if (getConnection() != null) {
            System.out.println("Kết nối thành công!");
        } else {
            System.out.println("Kết nối thất bại!");
        }
    }
}