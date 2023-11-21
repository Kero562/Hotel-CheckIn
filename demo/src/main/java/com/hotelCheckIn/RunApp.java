package com.hotelCheckIn;

public class RunApp {
    public static void main(String[] args)
    {
        DatabaseUtil dbManager = new DatabaseUtil();
        dbManager.initializeDB();

        // // Current Customer ID/UUID: 43333863
        // int customerId = dbManager.addCustomer("John", "Doe", "1234567890", "John.Doe@gmail.com");
        // if (customerId == -1) {
        //     System.out.println("Customer not added skipping room and reservation.");
        // } else {
        //     int roomNumber = dbManager.addRoom(101, 2, 100.00, "SingleRoom", "Empty");
        //     if (roomNumber == -1) {
        //         System.out.println("Room not added skipping reservation.");
        //     } else {
        //         // Maybe create a view for this
        //         // https://stackoverflow.com/questions/8753959/round-a-floating-point-number-to-the-next-integer-value-in-java
        //         // totalCost = (int) Math.ceil((checkout-checkin)/3600) * dailyRate
        //         dbManager.addReservation(customerId, roomNumber, 1698867509047L, 1704013122000L, "Pending");
        //     }
        // }

        // // 67311723
        // customerId = dbManager.addCustomer("Jenny", "Sanders", "8587986187", "jenny@gmail.com");
        // if (customerId == -1) {
        //     System.out.println("Customer not added skipping room and reservation.");
        // } else {
        //     int roomNumber = dbManager.addRoom(102, 4, 200.00, "DoubleRoom", "Empty");
        //     if (roomNumber == -1) {
        //         System.out.println("Room not added skipping reservation.");
        //     } else {
        //         // Maybe create a view for this
        //         // https://stackoverflow.com/questions/8753959/round-a-floating-point-number-to-the-next-integer-value-in-java
        //         // totalCost = (int) Math.ceil((checkout-checkin)/3600) * dailyRate
        //         dbManager.addReservation(customerId, roomNumber, 1698867509047L, 1704013122000L, "Pending");
        //     }
        // }

        // // 16338193
        // customerId = dbManager.addCustomer("Jacob", "Smith", "6195678934", "jsmith@gmail.com");
        // if (customerId == -1) {
        //     System.out.println("Customer not added skipping room and reservation.");
        // } else {
        //     int roomNumber = dbManager.addRoom(103, 4, 200.00, "DoubleRoom", "Empty");
        //     if (roomNumber == -1) {
        //         System.out.println("Room not added skipping reservation.");
        //     } else {
        //         // Maybe create a view for this
        //         // https://stackoverflow.com/questions/8753959/round-a-floating-point-number-to-the-next-integer-value-in-java
        //         // totalCost = (int) Math.ceil((checkout-checkin)/3600) * dailyRate
        //         dbManager.addReservation(customerId, roomNumber, 1698867509047L, 1704013122000L, "Pending");
        //     }
        // }

        // dbManager.addAdmin("admin", "password");
        
        String logView = "CREATE VIEW IF NOT EXISTS log AS "
        + "SELECT c.customer_id, c.last_name, c.email, r.room_number, r.reservation_status "
        + "FROM customer c "
        + "INNER JOIN reservation r ON c.customer_id = r.customer_id;";
        dbManager.executeStatement(logView);

        String serviceLog = "CREATE VIEW IF NOT EXISTS service_log AS\n"
        + "SELECT l.customer_id, l.last_name, l.email, l.room_number, l.reservation_status, service.type, service.urgency, service.status\n"
        + "FROM log l\n"
        + "LEFT JOIN service ON service.room_number = l.room_number;";
        dbManager.executeStatement(serviceLog);


        // Service service = new Service(101, "Towel", "Urgent");
        // Service service2 = new Service(101, "Key Card", "Urgent");


        // long currentTimestamp = System.currentTimeMillis();
        // System.out.println("The current epoch time is: " + currentTimestamp);

        Main.main(args);
    }
}
