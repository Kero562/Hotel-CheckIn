package com.hotelCheckIn;

public class RunApp {
    public static void main(String[] args)
    {
        DatabaseUtil dbManager = new DatabaseUtil();
        dbManager.initializeDB();

        // Current Customer ID/UUID: 21334406
        int customerId = dbManager.addCustomer("John", "Doe", "1234567890", "John.Doe@gmail.com");
        if (customerId == -1) {
            System.out.println("Customer not added skipping room and reservation.");
        } else {
            int roomNumber = dbManager.addRoom(101, 2, 100.00, "SingleRoom", "Empty");
            if (roomNumber == -1) {
                System.out.println("Room not added skipping reservation.");
            } else {
                // Maybe create a view for this
                // https://stackoverflow.com/questions/8753959/round-a-floating-point-number-to-the-next-integer-value-in-java
                // totalCost = (int) Math.ceil((checkout-checkin)/3600) * dailyRate
                dbManager.addReservation(customerId, roomNumber, 1698867509047L, 1704013122000L, "Pending");
            }
        }

        dbManager.addAdmin("admin", "password");
        
        String checkInLogView = "CREATE VIEW IF NOT EXISTS check_in_log AS\n"
        + "SELECT c.customer_id, c.first_name, c.last_name, c.phone, c.email, r.room_number, r.check_in_date, r.check_out_date, r.reservation_status\n"
        + "FROM customer c, reservation r\n"
        + "INNER JOIN reservation ON c.customer_id = r.customer_id;";
        dbManager.executeStatement(checkInLogView);

        // long currentTimestamp = System.currentTimeMillis();
        // System.out.println("The current epoch time is: " + currentTimestamp);

        Main.main(args);
    }
}
