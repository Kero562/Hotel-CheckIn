package com.hotelCheckIn;

public class RunApp {
    public static void main(String[] args)
    {
        DatabaseUtil.initializeDB("hotel.db");
        Main.main(args);
    }
}
