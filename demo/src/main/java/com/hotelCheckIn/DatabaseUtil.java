package com.hotelCheckIn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.RandomStringUtils;

public class DatabaseUtil {
    // Initialize Database file
    public void initializeDB() {
        //String url = "jdbc:sqlite:.\\demo\\src\\main\\java\\com\\hotelCheckIn\\db\\hotel.db";
        Connection conn = connect();
        if (conn != null) {
            //DatabaseMetaData meta = conn.getMetaData();
            //System.out.println("The driver name is " + meta.getDriverName());
            System.out.println("[Info] Database accessible.");

            String customer = "CREATE TABLE IF NOT EXISTS customer (\n"
            + "	customer_id integer PRIMARY KEY,\n"
            + "	first_name text NOT NULL,\n"
            + "	last_name text NOT NULL,\n"
            + "	phone text NOT NULL UNIQUE,\n"
            + "	email text NOT NULL UNIQUE\n"
            + ");";
            createTable("customer", customer);

            String admin = "CREATE TABLE IF NOT EXISTS admin (\n"
            + "	admin_id integer PRIMARY KEY,\n"
            + "	username text NOT NULL UNIQUE,\n"
            + "	password text NOT NULL UNIQUE\n"
            + ");";
            createTable("admin", admin);

            String room = "CREATE TABLE IF NOT EXISTS rooms (\n"
            + "	room_number integer PRIMARY KEY,\n"
            + " capacity integer NOT NULL,\n"
            + " daily_rate real NOT NULL,\n"
            + " room_type text NOT NULL,\n"
            + " room_status text NOT NULL\n"
            + ");";
            createTable("rooms", room);

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

            String service = "CREATE TABLE IF NOT EXISTS service (\n"
            + " service_id text PRIMARY KEY,\n"
            + " room_number integer NOT NULL,\n"
            + " type text NOT NULL,\n"
            + " description text NOT NULL,\n"
            + " urgency text NOT NULL,\n"
            + " time_created text NOT NULL,\n"
            + " service_status text NOT NULL,\n"
            + " assigned_employee text NOT NULL,\n"
            + " FOREIGN KEY (room_number) REFERENCES rooms (room_number)\n"
            + ");";
            createTable("service", service);
        }
        else {
            System.out.println("[Error] Database not accessible.");
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

    public void executeStatement(String sql) {
        try {
            Connection conn = this.connect();
            conn.createStatement().execute(sql);
            System.out.println("Executed Statement.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createTable(String tableName, String sql) {
        if (!hasTable(tableName)) {
            executeStatement(sql);
            System.out.println("[Info] Table " + tableName + " created.");
        }
        else {
            System.out.println("[Info] Table " + tableName + " already exists.");
        }
    }

    private boolean hasTable(String tableName) {
        try {
            Connection conn = this.connect();
            String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            String name = rs.getString("name");
            if (name != null) {
                System.out.println("Table " + tableName + " exists.");
                return true;
            } else {
                System.out.println("Table " + tableName + " does not exist.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Check customer reservation
    public boolean isValidLogin(String lastName, String strCustomerId) {        
        try {
            int customerId = Integer.parseInt(strCustomerId);
            Connection conn = this.connect();
            String customerQuery = "SELECT * FROM customer c WHERE c.last_name = '" + lastName + "' AND c.customer_id = '" + customerId + "'";
            boolean isValidCustomer = conn.createStatement().execute(customerQuery);

            String reservationQuery = "Select * FROM reservation r WHERE r.customer_id = '" + customerId + "'";
            ResultSet reservationTable = conn.createStatement().executeQuery(reservationQuery);
            long checkInDate = reservationTable.getLong("check_in_date");
            long checkOutDate = reservationTable.getLong("check_out_date");

            boolean hasPendingReservation = hasPendingReservation(customerId);


            if (checkInDate < System.currentTimeMillis() && checkOutDate > System.currentTimeMillis() && isValidCustomer && hasPendingReservation) {
                System.out.println("Valid reservation.");
                return true;
            }
            else if (!hasPendingReservation) {
                System.out.println("Customer does not have a pending reservation.");
                return false;
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

    public boolean hasPendingReservation(int customerId) {
        try {
            Connection conn = this.connect();
            String reservationQuery = "SELECT * FROM reservation r WHERE r.customer_id = '" + customerId + "'";
            ResultSet reservationTable = conn.createStatement().executeQuery(reservationQuery);
            do {
                String reservationStatus = reservationTable.getString("reservation_status");
                if (reservationStatus.equals("Pending")) {
                    System.out.println("Customer has a pending reservation.");
                    return true;
                }
            } while (reservationTable.next());

            System.out.println("Customer does not have a pending reservation.");
            return false;
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

    public int addCustomer(String firstName, String lastName, String phone, String email) {
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
