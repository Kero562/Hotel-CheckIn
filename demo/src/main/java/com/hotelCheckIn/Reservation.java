package com.hotelCheckIn;

import java.time.LocalDateTime;

public class Reservation {
   private int customerID;
   private LocalDateTime startTime;
   private LocalDateTime endTime;
   private int roomNumber;
   private String reservationStatus;

   Reservation(int customerID, LocalDateTime startTime, LocalDateTime endTime, int roomNumber,
         String reservationStatus) {
      this.customerID = customerID;
      this.startTime = startTime;
      this.endTime = endTime;
      this.roomNumber = roomNumber;
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
      return this.roomNumber;
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
      this.roomNumber = roomNumber;
   }

   public void setReservationStatus(String reservationStatus) {
      this.reservationStatus = reservationStatus;
   }
}
