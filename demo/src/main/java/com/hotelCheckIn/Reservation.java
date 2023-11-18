package com.hotelCheckIn;

import java.time.LocalDateTime;

public class Reservation {
   private int customerID;
   private LocalDateTime startTime;
   private LocalDateTime endTime;
   private String reservationStatus;
   private Room room;

   Reservation(int customerID, LocalDateTime startTime, LocalDateTime endTime, Room room,
         String reservationStatus) {
      this.customerID = customerID;
      this.startTime = startTime;
      this.endTime = endTime;
      this.room = room;
      this.reservationStatus = reservationStatus;
   }

   public int getCustomerID() {
      return this.customerID;
   }

   public LocalDateTime getStartTime() {
      return this.startTime;
   }

   public LocalDateTime getEndTime() {
      return this.endTime;
   }

   public int getRoomNumber() {
      return room.getRoomNumber();
   }

   public String getReservationStatus() {
      return this.reservationStatus;
   }

   public void setCustomerID(int customerID) {
      this.customerID = customerID;
   }

   public void setStartTime(LocalDateTime startTime) {
      this.startTime = startTime;
   }

   public void setEndTime(LocalDateTime endTime) {
      this.endTime = endTime;
   }

   public void setRoomNumber(int roomNumber) {
      room.setRoomNumber(roomNumber);
   }

   public void setReservationStatus(String reservationStatus) {
      this.reservationStatus = reservationStatus;
   }
}
