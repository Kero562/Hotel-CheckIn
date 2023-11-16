package com.hotelCheckIn;

import java.time.LocalDateTime;

public class Reservation {
   private float cost;
   private LocalDateTime startTime;
   private LocalDateTime endTime;
   // TODO: incorporate Room object

   Reservation(LocalDateTime startTime, LocalDateTime endTime) {
      this.startTime = startTime;
      this.endTime = endTime;
   }

   public float getCost() {
      return this.cost;
   }

   public LocalDateTime getStartTime() {
      return this.startTime;
   }

   public LocalDateTime getEndTime() {
      return this.endTime;
   }

   public void setStartTime(LocalDateTime startTime) {
      this.startTime = startTime;
   }

   public void setEndTime(LocalDateTime endTime) {
      this.endTime = endTime;
   }

   public void setRoom() {
      // TODO: add set room code
   }
}
