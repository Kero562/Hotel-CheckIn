package com.hotelCheckIn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Reservation {
   private int customerID;
   private long checkIn;
   private long checkOut;
   private String reservationStatus;
   private Room room;

   Reservation(int customerID, long checkIn, long checkOut, String reservationStatus, Room room) {
      this.customerID = customerID;
      this.checkIn = checkIn;
      this.checkOut = checkOut;
      this.reservationStatus = reservationStatus;
      this.room = room;
   }

   Reservation(int customerID) {
      this.customerID = customerID;
      String reservationQuery = "SELECT * FROM reservation WHERE customer_id = " + this.customerID;
      // Connect to the database
      DatabaseUtil dbManager = new DatabaseUtil();
      Connection conn = dbManager.connect();
      try {
         // Execute the query
         ResultSet rs = conn.createStatement().executeQuery(reservationQuery);
         // Get the results
         this.checkIn = rs.getLong("check_in_date");
         this.checkOut = rs.getLong("check_out_date");
         this.reservationStatus = rs.getString("reservation_status");
         this.room = new Room(rs.getInt("room_number"));
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public int getCustomerID() {
      return this.customerID;
   }

   public long getCheckIn() {
      return this.checkIn;
   }

   public long getCheckOut() {
      return this.checkOut;
   }

   public String getReservationStatus() {
      return this.reservationStatus;
   }

   public Room getRoom() {
      return this.room;
   }

   public int getRoomNumber() {
      return this.room.getRoomNumber();
   }

   public void setCustomerID(int customerID) {
      this.customerID = customerID;
   }

   public void setStartTime(long checkIn) {
      this.checkIn = checkIn;
   }

   public void setEndTime(long checkOut) {
      this.checkOut = checkOut;
   }

   public void setReservationStatus(String reservationStatus) {
      this.reservationStatus = reservationStatus;
      // Connect to the database
      DatabaseUtil dbManager = new DatabaseUtil();
      Connection conn = dbManager.connect();
      try {
         // Execute the query
         conn.createStatement().executeUpdate("UPDATE reservation SET reservation_status = '" + this.reservationStatus + "' WHERE customer_id = " + this.customerID);
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public void setRoom(Room room) {
      this.room = room;
   }

   public void setRoomNumber(int roomNumber) {
      this.room.setRoomNumber(roomNumber);
   }

   @Override
   public String toString() {
      return "Reservation{" +
              "customerID=" + this.customerID +
              ", checkIn=" + this.checkIn +
              ", checkOut=" + this.checkOut +
              ", reservationStatus='" + this.reservationStatus + '\'' +
              ", room=\n\t\t" + this.room +
              "\n\t}";
   }
}
