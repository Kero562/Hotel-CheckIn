package com.hotelCheckIn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.RandomStringUtils;

public class DatabaseUtil {

    // Initialize Database file
    public void initializeDB() {
        Connection conn = connect();
        if (conn != null) {
            System.out.println("[Info] Database accessible.");

            // Create a customer database with customer_id, first_name, last_name, phone, email
            String customer = "CREATE TABLE IF NOT EXISTS customer (\n"
            + "	customer_id integer PRIMARY KEY,\n"
            + "	first_name text NOT NULL,\n"
            + "	last_name text NOT NULL,\n"
            + "	phone text NOT NULL UNIQUE,\n"
            + "	email text NOT NULL UNIQUE\n"
            + ");";
            createTable("customer", customer);

            // Create an admin database with admin_id, username, password
            String admin = "CREATE TABLE IF NOT EXISTS admin (\n"
            + "	admin_id integer PRIMARY KEY,\n"
            + "	username text NOT NULL UNIQUE,\n"
            + "	password text NOT NULL UNIQUE\n"
            + ");";
            createTable("admin", admin);

            // Create a rooms database with room_number, capacity, daily_rate, room_type, room_status
            String room = "CREATE TABLE IF NOT EXISTS rooms (\n"
            + "	room_number integer PRIMARY KEY,\n"
            + " capacity integer NOT NULL,\n"
            + " daily_rate real NOT NULL,\n"
            + " room_type text NOT NULL,\n"
            + " room_status text NOT NULL\n"
            + ");";
            createTable("rooms", room);

            // Create a reservation database with customer_id, room_number, check_in_date, check_out_date, reservation_status
            String reservation = "CREATE TABLE IF NOT EXISTS reservation (\n"
            + " customer_id integer NOT NULL,\n"
            + " room_number integer NOT NULL,\n"
            + " check_in_date integer NOT NULL,\n"
            + " check_out_date integer NOT NULL,\n"
            + " reservation_status text NOT NULL,\n"
            + " PRIMARY KEY (customer_id, room_number),\n"
            + " FOREIGN KEY (customer_id) REFERENCES customer (customer_id),\n"
            + " FOREIGN KEY (room_number) REFERENCES rooms (room_number)\n"
            + ");";
            createTable("reservation", reservation);

            // Create a service database with service_id, room_number, type, description, urgency, time_created, service_status, assigned_employee
            String service = "CREATE TABLE IF NOT EXISTS service (\n"
            + " service_id integer PRIMARY KEY,\n"
            + " room_number integer NOT NULL,\n"
            + " type text NOT NULL,\n"
            + " urgency text NOT NULL,\n"
            + " FOREIGN KEY (room_number) REFERENCES rooms (room_number)\n"
            + ");";
            createTable("service", service);
        }
        else {
            System.out.println("[Error] Database not accessible.");
        }
    }

    // private method to connect to the database
    public Connection connect() {
        // SQLite connection string to the database file
        String url = "jdbc:sqlite:.\\demo\\src\\main\\java\\com\\hotelCheckIn\\db\\hotel.db";
        Connection conn = null;
        // Try to connect to the database
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            // Print error message if connection fails
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // Execute any generic SQL statement that does require a return value
    public void executeStatement(String sql) {
        try {
            Connection conn = this.connect();
            conn.createStatement().execute(sql);
            System.out.println("Executed Statement.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Wrapper method pairing table name with SQL statement to prevent duplicate tables
    // without the need to rely on SQlite's "IF NOT EXISTS" clause 
    public void createTable(String tableName, String sql) {
        // Check if table exists
        if (!hasTable(tableName)) {
            // Create table if it does not exist
            executeStatement(sql);
            System.out.println("[Info] Table " + tableName + " created.");
        }
        else {
            // Do nothing if table exists
            System.out.println("[Info] Skipping creation " + tableName + " already exists.");
        }
    }

    private boolean hasTable(String tableName) {
        try {
            // connect to the database
            Connection conn = this.connect();
            // SQL statement to get table from the master schema where the name is equal to the argument passes in
            String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            // Get the name of the table, which sould just be the argument passed in
            String name = rs.getString("name");
            if (name != null) {
                // Table exists
                System.out.println("[Info] Table " + tableName + " exists.");
                return true;
            } else {
                // Table does not exist
                System.out.println("[Info] Table " + tableName + " does not exist.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Check customer login information and then check if the customer has a pending reservation
    public boolean isValidLogin(String lastName, String strCustomerId) {        
        try {
            // Convert customer ID to an integer since it is passed in by the UI as a String
            int customerId = Integer.parseInt(strCustomerId);

            // Connect to the database
            Connection conn = this.connect();

            // Query the customer table for the customer with the last name and customer ID passed in
            String customerQuery = "SELECT * FROM customer c WHERE c.last_name = '" + lastName + "' AND c.customer_id = '" + customerId + "'";
            // exeute the query and save the boolean value indicating whether or not there is a customer with the last name and customer ID passed in
            boolean isValidCustomer = conn.createStatement().execute(customerQuery);

            // Query the reservation table for the reservation with the customer ID passed in
            String reservationQuery = "Select * FROM reservation r WHERE r.customer_id = '" + customerId + "'";
            ResultSet reservationTable = conn.createStatement().executeQuery(reservationQuery);
            // Get the check in date and check out date from the reservation table
            long checkInDate = reservationTable.getLong("check_in_date");
            long checkOutDate = reservationTable.getLong("check_out_date");

            // Check the status of the reservation to make sure it is pending aka the user has not yet checked in
            boolean hasPendingReservation = hasPendingReservation(customerId);

            // Perfect scenario: the customer has a pending reservation and the check in date is before the current time and the check out date is after the current time
            if (checkInDate < System.currentTimeMillis() && checkOutDate > System.currentTimeMillis() && isValidCustomer && hasPendingReservation) {
                System.out.println("[Info] Valid reservation.");
                return true;
            }
            // Customer does not exist
            else if (!isValidCustomer) {
                System.out.println("[Info] Customer does not exist.");
                return false;
            }
            // Customer does not have a pending reservation
            else if (!hasPendingReservation) {
                System.out.println("[Info] Customer does not have a pending reservation.");
                return false;
            }
            // Reservation is not valid yet, the guest must wait until the check in date
            else if (checkInDate > System.currentTimeMillis()) {
                System.out.println("[Info] Reservation is not valid yet.");
                return false;
            }
            // Reservation has expired
            else if (checkInDate < System.currentTimeMillis()) {
                System.out.println("[Info] Reservation has expired.");
                return false;
            }
            // Fallback
            else {
                System.out.println("[Info] Invalid reservation.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean hasPendingReservation(int customerId) {
        try {
            Connection conn = this.connect();
            // Query the reservation table for all reservations associated with the passed customerId
            String reservationQuery = "SELECT * FROM reservation r WHERE r.customer_id = '" + customerId + "'";
            ResultSet reservationTable = conn.createStatement().executeQuery(reservationQuery);
            // Loop through all the reservations associated with the passed customerId anc check them
            while (reservationTable.next()) {
                // Get the reservation status
                String reservationStatus = reservationTable.getString("reservation_status");
                if (reservationStatus.equals("Pending")) {
                    System.out.println("[Info] Customer has a pending reservation.");
                    return true;
                }
            }

            // If the loop completes without finding a pending reservation then the customer does not have a pending reservation
            System.out.println("[Info] Customer does not have a pending reservation.");
            return false;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean isAdmin(String username, String password) {
        try {
            Connection conn = this.connect();

            // Query the admin table for the admin with the username and password passed in
            String adminQuery = "SELECT * FROM admin a WHERE a.username = '" + username + "' AND a.password = '" + password + "'";
            ResultSet adminTable = conn.createStatement().executeQuery(adminQuery);
            // Get the admin ID from the admin table to verify if there was a row found
            String adminID = adminTable.getString("admin_id");
            if (adminID != null) {
                System.out.println("[Info] Valid admin: " + username + " with UUID " + adminID);
                return true;
            } else {
                System.out.println("[Info] Invalid admin: " + username + " with password " + password);
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public int addAdmin(String username, String password) {
        // Generate a random 8 digit number for the admin ID
        int adminId = Integer.parseInt(RandomStringUtils.randomNumeric(8)); 
        // Insert the admin into the admin table
        String sql = "INSERT INTO admin (admin_id, username, password) VALUES ('" + adminId + "', '" + username + "', '" + password + "')";
        try {
            Connection conn = this.connect();
            conn.createStatement().execute(sql);
            System.out.println("[Info] Admin has been added with ID: " + adminId);
            // Return the admin ID upon success
            return adminId;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // Return -1 upon failure
            return -1;
        }
    }

    public int addCustomer(String firstName, String lastName, String phone, String email) {
        // Generate a random 8 digit number for the customer ID
        int customerId = Integer.parseInt(RandomStringUtils.randomNumeric(8));
        // Insert the customer into the customer table
        String sql = "INSERT INTO customer (customer_id, first_name, last_name, phone, email) VALUES ('" + customerId + "', '" + firstName + "', '" + lastName + "', '" + phone + "', '" + email + "')";
        try {
            Connection conn = this.connect();
            conn.createStatement().execute(sql);
            System.out.println("[Info] Customer has been added with ID: " + customerId);
            // Return the customer ID upon success
            return customerId;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // Return -1 upon failure
            return -1;
        }
    }

    public int addRoom (int roomNumber, int capacity, double dailyRate, String roomType, String roomStatus) {
        // Insert the room into the room table
        String sql = "INSERT INTO rooms (room_number, capacity, daily_rate, room_type, room_status) VALUES ('" + roomNumber + "', '" + capacity + "', '" + dailyRate + "', '" + roomType + "', '" + roomStatus + "')";
        try {
            Connection conn = this.connect();
            conn.createStatement().execute(sql);
            System.out.println("[Info] Room has been added.");
            // Return the room number upon success
            return roomNumber;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // Return -1 upon failure
            return -1;
        }
    }

    public void addReservation (int customerID, int roomNumber, long checkInDate, long checkOutDate, String reservationStatus) {
        // Insert the reservation into the reservation table
        String sql = "INSERT INTO reservation (customer_id, room_number, check_in_date, check_out_date, reservation_status) VALUES ('" + customerID + "', '" + roomNumber + "', '" + checkInDate + "', '" + checkOutDate + "', '" + reservationStatus + "')";
        try {
            Connection conn = this.connect();
            conn.createStatement().execute(sql);
            System.out.println("[Info] Reservation has been added.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setReservationStatus (int customerId, int roomNumber, String status) {
        // Update the reservation status in the reservation table
        String sql = "UPDATE reservation SET reservation_status = '" + status + "' WHERE customer_id = '" + customerId + "' AND room_number = '" + roomNumber + "'";
        try {
            Connection conn = this.connect();
            conn.createStatement().execute(sql);
            System.out.println("[Info] Reservation status has been updated.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setRoomStatus (int roomNumber, String status) {
        // Update the room status in the room table
        String sql = "UPDATE rooms SET room_status = '" + status + "' WHERE room_number = '" + roomNumber + "'";
        try {
            Connection conn = this.connect();
            conn.createStatement().execute(sql);
            System.out.println("Room status has been updated.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Check out a customer by updating the reservation status and room status
    public void checkIn(int customerId, int roomNumber) {
        setReservationStatus(customerId, roomNumber, "Checked In");
        setRoomStatus(roomNumber, "Occupied");
    }
}
