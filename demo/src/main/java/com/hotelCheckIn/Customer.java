package com.hotelCheckIn;

import java.util.List;

public class Customer {
   private int customerID;
   private String firstName;
   private String lastName;
   private String emailAddress;
   private int phoneNumber;
   private List<Reservation> reservations;

   Customer(int customerID, String firstName, String lastName, String emailAddress, int phoneNumber) {
      this.customerID = customerID;
      this.firstName = firstName;
      this.lastName = lastName;
      this.emailAddress = emailAddress;
      this.phoneNumber = phoneNumber;
   }

   public int getCustomerID(){
      return this.customerID;
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

   public void setCustomerID(int customerID){
      this.customerID = customerID;
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