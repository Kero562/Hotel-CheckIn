package com.hotelCheckIn;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    // Initialize Database file
    public static void initializeDB(String fileName) {
        String url = "jdbc:sqlite:.\\demo\\src\\main\\java\\com\\hotelCheckIn\\db\\" + fileName;
        try {
            Connection conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("Database " + fileName + " has been created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Initialize tables
    public static void executeStatement(String sql) {
        String url = "jdbc:sqlite:.\\demo\\src\\main\\java\\com\\hotelCheckIn\\db\\hotel.db";
        try {
            Connection conn = DriverManager.getConnection(url);
            conn.createStatement().execute(sql);
            System.out.println("Table has been created.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
