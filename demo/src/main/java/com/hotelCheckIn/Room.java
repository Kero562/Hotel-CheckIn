package com.hotelCheckIn;

public class Room {
   private int roomNumber;
   private int capacity;
   private float dailyRate;
   private String roomType;
   private String roomStatus;

   Room(int roomNumber, int capacity, float dailyRate, String roomType, String roomStatus) {
      this.roomNumber = roomNumber;
      this.capacity = capacity;
      this.dailyRate = dailyRate;
      this.roomType = roomType;
      this.roomStatus = roomStatus;
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

   public String getRoomType() {
      return this.roomType;
   }

   public String getRoomStatus() {
      return this.roomStatus;
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

   public void setRoomType(String roomType) {
      this.roomType = roomType;
   }

   public void setRoomStatus(String roomStatus) {
      this.roomStatus = roomStatus;
   }
}
