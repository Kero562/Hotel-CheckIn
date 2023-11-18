package com.hotelCheckIn;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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

   Customer(int customerID) {
      this.customerID = customerID;
      String customerQuery = "SELECT * FROM customer WHERE customer_id = " + this.customerID;
      // Connect to the database
      DatabaseUtil dbManager = new DatabaseUtil();
      Connection conn = dbManager.connect();
      try {
         // Execute the query
         ResultSet rs = conn.createStatement().executeQuery(customerQuery);
         // Get the results
         this.firstName = rs.getString("first_name");
         this.lastName = rs.getString("last_name");
         this.emailAddress = rs.getString("email");
         this.phoneNumber = rs.getInt("phone");

         // Get the reservations
         this.reservations = new ArrayList<Reservation>();
         ResultSet rs2 = conn.createStatement().executeQuery("SELECT * FROM reservation WHERE customer_id = " + this.customerID);
         while (rs2.next()) {
            //Reservation reservation = new Reservation(rs2.getInt("customer_id"), rs2.getLong("check_in_date"), rs2.getLong("check_out_date"), rs2.getString("reservation_status"));
            Reservation reservation = new Reservation(rs2.getInt("customer_id"));
            this.reservations.add(reservation);
         }
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
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

   public List<Reservation> getReservations() {
      return this.reservations;
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

   @Override
   public String toString() {
      return "Customer{" +
              "customerID=" + this.customerID +
              ", firstName='" + this.firstName + '\'' +
              ", lastName='" + this.lastName + '\'' +
              ", emailAddress='" + this.emailAddress + '\'' +
              ", phoneNumber=" + this.phoneNumber + '\'' +
              ", reservations=\n\t" + this.reservations +
              "\n}";
   }
}