package com.hotelCheckIn;

public class Room {
   private int roomNumber;
   private int capacity;
   private float dailyRate;
   private String roomType;

   private Room(int roomNumber, int capacity, float dailyRate) {
      this.roomNumber = roomNumber;
      this.capacity = capacity;
      this.dailyRate = dailyRate;
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
}
