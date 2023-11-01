package com.hotelCheckIn;

public class RunApp {
    public static void main(String[] args)
    {
        DatabaseUtil.initializeDB("hotel.db");

        String customer = "CREATE TABLE IF NOT EXISTS customer (\n"
        + "	customer_id integer PRIMARY KEY,\n"
        + "	firstName text NOT NULL,\n"
        + "	lastName text NOT NULL,\n"
        + "	phone text NOT NULL UNIQUE,\n"
        + "	email text NOT NULL UNIQUE\n"
        + ");";
        DatabaseUtil.executeStatement(customer);

        String room = "CREATE TABLE IF NOT EXISTS rooms (\n"
        + "	room_number integer PRIMARY KEY,\n"
        + " capacity integer NOT NULL,\n"
        + " daily_rate real NOT NULL,\n"
        + " room_type text NOT NULL,\n"
        + " room_status text NOT NULL\n"
        + ");";
        DatabaseUtil.executeStatement(room);

        String reservation = "CREATE TABLE IF NOT EXISTS reservation (\n"
        + " customer_id integer NOT NULL,\n"
        + " room_number integer NOT NULL,\n"
        + " check_in_date text NOT NULL,\n"
        + " check_out_date text NOT NULL,\n"
        + " total_cost real NOT NULL,\n"
        + " PRIMARY KEY (customer_id, room_number),\n"
        + " FOREIGN KEY (customer_id) REFERENCES customer (customer_id),\n"
        + " FOREIGN KEY (room_number) REFERENCES rooms (room_number)\n"
        + ");";
        DatabaseUtil.executeStatement(reservation);

        // Maybe we add another table for employee
        String service = "CREATE TABLE IF NOT EXISTS service (\n"
        + " service_id integer PRIMARY KEY,\n"
        + " room_number integer NOT NULL,\n"
        + " type text NOT NULL,\n"
        + " description text NOT NULL,\n"
        + " urgency text NOT NULL,\n"
        + " time_created text NOT NULL,\n"
        + " service_status text NOT NULL,\n"
        + " assigned_employee text NOT NULL,\n"
        + " FOREIGN KEY (room_number) REFERENCES rooms (room_number)\n"
        + ");";
        DatabaseUtil.executeStatement(service);

        Main.main(args);
    }
}
