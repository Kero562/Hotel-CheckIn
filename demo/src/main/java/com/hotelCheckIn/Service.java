package com.hotelCheckIn;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.lang3.RandomStringUtils;

public class Service {
   private int serviceID;
   private int roomNumber;
   private String type;
   private String urgency;

   Service(int roomNumber, String type, String urgency) {
      this.serviceID = Integer.parseInt(RandomStringUtils.randomNumeric(8));
      this.roomNumber = roomNumber;
      this.type = type;
      this.urgency = urgency;

      System.out.println("[DEBUG] Creating Service: " + this.serviceID + " " + this.roomNumber + " " + this.type + " " + this.urgency);


      DatabaseUtil dbManager = new DatabaseUtil();
      Connection conn = dbManager.connect();
      // Insert the service into the database
      String insertService = "INSERT INTO service (service_id, room_number, type, urgency) VALUES (" + this.serviceID + ", " + this.roomNumber + ", '" + this.type + "', '" + this.urgency + "');";
      try {
         conn.createStatement().execute(insertService);
         conn.close();
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public int getServiceID() {
      return this.serviceID;
   }

   public void setServiceID(int serviceID) {
      this.serviceID = serviceID;
   }

   public void setUrgency(String urgency) {
      this.urgency = urgency;
   }

   public String getUrgency()
   {
      return this.urgency;
   }
}
