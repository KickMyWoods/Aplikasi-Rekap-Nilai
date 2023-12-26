package com.rekap.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {
    // Melakukan Koneksi ke Database
    public static Connection getConnection() throws SQLException {
        // URL Database
        String url = "jdbc:mysql://localhost:3306/rekapnilai";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }
}
