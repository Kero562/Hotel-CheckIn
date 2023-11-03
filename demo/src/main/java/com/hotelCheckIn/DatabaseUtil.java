package com.hotelCheckIn;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseUtil {
    // Initialize Database file
    public void initializeDB() {
        String url = "jdbc:sqlite:.\\demo\\src\\main\\java\\com\\hotelCheckIn\\db\\hotel.db";
        try {
            Connection conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("Database has been created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:.\\demo\\src\\main\\java\\com\\hotelCheckIn\\db\\hotel.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // Initialize tables
    public void executeStatement(String sql) {
        try {
            Connection conn = this.connect();
            conn.createStatement().execute(sql);
            System.out.println("Table has been created.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Check customer reservation
    public boolean isValidReservation(String lastName, String phoneNumber) {        
        try {
            Connection conn = this.connect();
            String customerQuery = "SELECT * FROM customer c WHERE c.lastName = '" + lastName + "' AND c.phone = '" + phoneNumber + "'";
            ResultSet customerTable = conn.createStatement().executeQuery(customerQuery);
            String customerID = customerTable.getString("customer_id");

            String reservationQuery = "Select * FROM reservation r WHERE r.customer_id = '" + customerID + "'";
            ResultSet reservationTable = conn.createStatement().executeQuery(reservationQuery);
            long checkInDate = reservationTable.getLong("check_in_date");
            long checkOutDate = reservationTable.getLong("check_out_date");

            if (checkInDate < System.currentTimeMillis() && checkOutDate > System.currentTimeMillis()) {
                System.out.println("Valid reservation.");
                return true;
            } else {
                System.out.println("Invalid reservation.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Add customer to database
    public String addCustomer(String firstName, String lastName, String phone, String email) {
        String customerID = UUID.randomUUID().toString();
        String sql = "INSERT INTO customer (customer_id, firstName, lastName, phone, email) VALUES ('" + customerID + "', '" + firstName + "', '" + lastName + "', '" + phone + "', '" + email + "')";
        try {
            Connection conn = this.connect();
            conn.createStatement().execute(sql);
            System.out.println("Customer has been added with ID: " + customerID);
            return customerID;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "N/A";
        }
    }

    public int addRoom (int roomNumber, int capacity, double dailyRate, String roomType, String roomStatus) {
        String sql = "INSERT INTO rooms (room_number, capacity, daily_rate, room_type, room_status) VALUES ('" + roomNumber + "', '" + capacity + "', '" + dailyRate + "', '" + roomType + "', '" + roomStatus + "')";
        try {
            Connection conn = this.connect();
            conn.createStatement().execute(sql);
            System.out.println("Room has been added.");
            return roomNumber;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public void addReservation (String customerID, int roomNumber, long checkInDate, long checkOutDate, double totalCost) {
        String sql = "INSERT INTO reservation (customer_id, room_number, check_in_date, check_out_date, total_cost) VALUES ('" + customerID + "', '" + roomNumber + "', '" + checkInDate + "', '" + checkOutDate + "', '" + totalCost + "')";
        try {
            Connection conn = this.connect();
            conn.createStatement().execute(sql);
            System.out.println("Reservation has been added.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
