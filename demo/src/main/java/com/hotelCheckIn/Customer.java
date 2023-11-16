package com.hotelCheckIn;

public class Customer {
   private String firstName;
   private String lastName;
   private String emailAddress;
   private int phoneNumber;

   Customer(String firstName, String lastName, String emailAddress, int phoneNumber) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.emailAddress = emailAddress;
      this.phoneNumber = phoneNumber;
   }

   public String getFullName() {
      return this.firstName + " " + this.lastName;
   }

   public String getFirstName() {
      return this.firstName;
   }

   public String getLastName() {
      return this.lastName;
   }

   public String getEmailAddress() {
      return this.emailAddress;
   }

   public int getPhoneNumber() {
      return this.phoneNumber;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public void setEmailAddress(String emailAddress) {
      this.emailAddress = emailAddress;
   }

   public void setPhoneNumber(int phoneNumber) {
      this.phoneNumber = phoneNumber;
   }
}