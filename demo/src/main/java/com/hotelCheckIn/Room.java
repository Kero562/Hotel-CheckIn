package com.hotelCheckIn;

public abstract class Room {
   private int roomNumber;
   private int capacity;
   private float dailyRate;

   private Room(int roomNumber, int capacity, float dailyRate) {
      this.roomNumber = roomNumber;
      this.capacity = capacity;
      this.dailyRate = dailyRate;
   }

   //Implemented across all subclasses
   public abstract String getRoomType();

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

   public static class SingleRoom extends Room {

      public SingleRoom(int roomNumber, int capacity, float dailyRate) {
         super(roomNumber, capacity, dailyRate);
      }

      public String getRoomType() {
         return "Single";
      }
   }

   public static class DoubleRoom extends Room {

      public DoubleRoom(int roomNumber, int capacity, float dailyRate) {
         super(roomNumber, capacity, dailyRate);
      }

      public String getRoomType() {
         return "Double";
      }
   }

   public static class TwinRoom extends Room {

      public TwinRoom(int roomNumber, int capacity, float dailyRate) {
         super(roomNumber, capacity, dailyRate);
   }

      public String getRoomType() {
         return "Twin";
      }
      
   }
}
