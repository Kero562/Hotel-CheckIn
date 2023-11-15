package com.hotelCheckIn;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;

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
    public boolean isValidReservation(String lastName, String strCustomerId) {        
        try {
            int customerId = Integer.parseInt(strCustomerId);
            Connection conn = this.connect();
            String customerQuery = "SELECT * FROM customer c WHERE c.last_name = '" + lastName + "' AND c.customer_id = '" + customerId + "'";
            boolean isValidCustomer = conn.createStatement().execute(customerQuery);

            String reservationQuery = "Select * FROM reservation r WHERE r.customer_id = '" + customerId + "'";
            ResultSet reservationTable = conn.createStatement().executeQuery(reservationQuery);
            long checkInDate = reservationTable.getLong("check_in_date");
            long checkOutDate = reservationTable.getLong("check_out_date");

            if (checkInDate < System.currentTimeMillis() && checkOutDate > System.currentTimeMillis() && isValidCustomer) {
                System.out.println("Valid reservation.");
                return true;
            }
            else if (checkInDate > System.currentTimeMillis() && checkOutDate > System.currentTimeMillis() && isValidCustomer) {
                System.out.println("Reservation is not valid yet.");
                return false;
            }
            else if (checkInDate < System.currentTimeMillis() && checkOutDate < System.currentTimeMillis() && isValidCustomer) {
                System.out.println("Reservation has expired.");
                return false;
            }
            else {
                System.out.println("Invalid reservation.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean isAdmin(String username, String password) {
        try {
            Connection conn = this.connect();
            String adminQuery = "SELECT * FROM admin a WHERE a.username = '" + username + "' AND a.password = '" + password + "'";
            ResultSet adminTable = conn.createStatement().executeQuery(adminQuery);
            String adminID = adminTable.getString("admin_id");
            if (adminID != null) {
                System.out.println("Valid admin: " + username + " with UUID " + adminID);
                return true;
            } else {
                System.out.println("Invalid admin: " + username + " with password " + password);
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public int addAdmin(String username, String password) {
        int adminId = Integer.parseInt(RandomStringUtils.randomNumeric(8)); 
        String sql = "INSERT INTO admin (admin_id, username, password) VALUES ('" + adminId + "', '" + username + "', '" + password + "')";
        try {
            Connection conn = this.connect();
            conn.createStatement().execute(sql);
            System.out.println("Admin has been added with ID: " + adminId);
            return adminId;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    // Add customer to database
    public int addCustomer(String firstName, String lastName, String phone, String email) {
        // https://stackoverflow.com/questions/4267475/generating-8-character-only-uuids
        //String customerID = UUID.randomUUID().toString();
        int customerId = Integer.parseInt(RandomStringUtils.randomNumeric(8)); 
        String sql = "INSERT INTO customer (customer_id, first_name, last_name, phone, email) VALUES ('" + customerId + "', '" + firstName + "', '" + lastName + "', '" + phone + "', '" + email + "')";
        try {
            Connection conn = this.connect();
            conn.createStatement().execute(sql);
            System.out.println("Customer has been added with ID: " + customerId);
            return customerId;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
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

    public void addReservation (int customerID, int roomNumber, long checkInDate, long checkOutDate, String reservationStatus) {
        String sql = "INSERT INTO reservation (customer_id, room_number, check_in_date, check_out_date, reservation_status) VALUES ('" + customerID + "', '" + roomNumber + "', '" + checkInDate + "', '" + checkOutDate + "', '" + reservationStatus + "')";
        try {
            Connection conn = this.connect();
            conn.createStatement().execute(sql);
            System.out.println("Reservation has been added.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setReservationStatus (int customerId, int roomNumber, String status) {
        // Checkin function for room and calls this func
        String sql = "UPDATE reservation SET reservation_status = '" + status + "' WHERE customer_id = '" + customerId + "' AND room_number = '" + roomNumber + "'";
        try {
            Connection conn = this.connect();
            conn.createStatement().execute(sql);
            System.out.println("Reservation status has been updated.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setRoomStatus (int roomNumber, String status) {
        String sql = "UPDATE rooms SET room_status = '" + status + "' WHERE room_number = '" + roomNumber + "'";
        try {
            Connection conn = this.connect();
            conn.createStatement().execute(sql);
            System.out.println("Room status has been updated.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void checkIn(int customerId, int roomNumber) {
        setReservationStatus(customerId, roomNumber, "Checked In");
        setRoomStatus(roomNumber, "Occupied");
    }
}
