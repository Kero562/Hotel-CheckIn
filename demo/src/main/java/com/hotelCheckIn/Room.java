package com.hotelCheckIn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Room {
   private int roomNumber;
   private int capacity;
   private float dailyRate;
   private String roomType;

   private Room(int roomNumber, int capacity, float dailyRate, String roomType) {
      this.roomNumber = roomNumber;
      this.capacity = capacity;
      this.dailyRate = dailyRate;
      this.roomType = roomType;
   }

   public Room(int roomNumber) {
      this.roomNumber = roomNumber;
      String roomQuery = "SELECT * FROM rooms WHERE room_number = " + this.roomNumber;
      // Connect to the database
      DatabaseUtil dbManager = new DatabaseUtil();
      Connection conn = dbManager.connect();
      try {
         // Execute the query
         ResultSet rs = conn.createStatement().executeQuery(roomQuery);
         // Get the results
         this.capacity = rs.getInt("capacity");
         this.dailyRate = rs.getFloat("daily_rate");
         this.roomType = rs.getString("room_type");
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public int getRoomNumber() {
      return this.roomNumber;
   }

   public int getCapacity() {
      return this.capacity;
   }

   public float getDailyRate() {
      return this.dailyRate;
   }

   public void setRoomNumber(int roomNumber) {
      this.roomNumber = roomNumber;
   }

   public void setCapacity(int capacity) {
      this.capacity = capacity;
   }

   public void setDailyRate(float dailyRate) {
      this.dailyRate = dailyRate;
   }

   public String getRoomType()
   {
      return this.roomType;
   }

   @Override
   public String toString() {
      return "Room{" +
              "roomNumber=" + this.roomNumber + 
              ", capacity=" + this.capacity + 
              ", dailyRate=" + this.dailyRate + 
              ", roomType=" + this.roomType + 
              "}";
   }
}
