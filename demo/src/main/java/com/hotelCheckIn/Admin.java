package com.hotelCheckIn;

public class Admin {
   private String adminID;
   private String username;
   private String password;

   Admin(String adminID, String username, String password) {
      this.adminID = adminID;
      this.username = username;
      this.password = password;
   }

   public String getAdminID() {
      return this.adminID;
   }

   public String getUsername() {
      return this.username;
   }

   public String getPassword() {
      return this.password;
   }

   public void setAdminID(String adminID) {
      this.adminID = adminID;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public void setPassword(String password) {
      this.password = password;
   }
}
