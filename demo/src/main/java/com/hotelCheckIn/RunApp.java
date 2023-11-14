package com.hotelCheckIn;

public class RunApp {
    public static void main(String[] args)
    {
        DatabaseUtil dbManager = new DatabaseUtil();
        dbManager.initializeDB();

        String customer = "CREATE TABLE IF NOT EXISTS customer (\n"
        + "	customer_id text PRIMARY KEY,\n"
        + "	firstName text NOT NULL,\n"
        + "	lastName text NOT NULL,\n"
        + "	phone text NOT NULL UNIQUE,\n"
        + "	email text NOT NULL UNIQUE\n"
        + ");";
        dbManager.executeStatement(customer);

        String room = "CREATE TABLE IF NOT EXISTS rooms (\n"
        + "	room_number integer PRIMARY KEY,\n"
        + " capacity integer NOT NULL,\n"
        + " daily_rate real NOT NULL,\n"
        + " room_type text NOT NULL,\n"
        + " room_status text NOT NULL\n"
        + ");";
        dbManager.executeStatement(room);

        /*
        dates stored in epoch time format such as 1698867509047 where the 
        last 3 digits are the milliseconds (different from some other epoch 
        formats which only go to the second) 
        */
        String reservation = "CREATE TABLE IF NOT EXISTS reservation (\n"
        + " customer_id text NOT NULL,\n"
        + " room_number integer NOT NULL,\n"
        + " check_in_date integer NOT NULL,\n"
        + " check_out_date integer NOT NULL,\n"
        + " total_cost real NOT NULL,\n"
        + " PRIMARY KEY (customer_id, room_number),\n"
        + " FOREIGN KEY (customer_id) REFERENCES customer (customer_id),\n"
        + " FOREIGN KEY (room_number) REFERENCES rooms (room_number)\n"
        + ");";
        dbManager.executeStatement(reservation);

        // Maybe we add another table for employee
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
        dbManager.executeStatement(service);

        // Current Customer ID/UUID: 6bada125-7e63-460e-a086-5b7b4f97e8bb
        String customerId = dbManager.addCustomer("John", "Doe", "1234567890", "John.Doe@gmail.com");
        if (customerId.equals("N/A")) {
            System.out.println("Customer not added skipping room and reservation.");
        } else {
            int roomNumber = dbManager.addRoom(101, 2, 100.00, "Standard", "Available");
            if (roomNumber == -1) {
                System.out.println("Room not added skipping reservation.");
            } else {
                // Maybe create a view for this
                // https://stackoverflow.com/questions/8753959/round-a-floating-point-number-to-the-next-integer-value-in-java
                // totalCost = (int) Math.ceil((checkout-checkin)/3600) * dailyRate
                dbManager.addReservation(customerId, roomNumber, 1698867509047L, 1704013122000L, 400.00);
            }
        }
        

        long currentTimestamp = System.currentTimeMillis();
        System.out.println("The current epoch time is: " + currentTimestamp);

        Main.main(args);
    }
}
