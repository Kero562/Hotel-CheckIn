package com.hotelCheckIn;

import java.time.LocalDateTime;

public class Service {
   private int serviceID;
   private int roomNumber;
   private String type;
   private String description;
   private String urgency;
   private LocalDateTime timeCreated;
   private String serviceStatus;
   private String assignedEmployee;

   Service(int serviceID, int roomNumber, String type, String description, String urgency, LocalDateTime timeCreated,
         String serviceStatus, String assignedEmployee) {
      this.serviceID = serviceID;
      this.roomNumber = roomNumber;
      this.type = type;
      this.description = description;
      this.urgency = urgency;
      this.timeCreated = timeCreated;
      this.serviceStatus = serviceStatus;
      this.assignedEmployee = assignedEmployee;
   }

   public int getServiceID() {
      return this.serviceID;
   }

   public int getRoomNumber() {
      return this.roomNumber;
   }

   public String getType() {
      return this.type;
   }

   public String getDesription() {
      return this.description;
   }

   public String getUrgency() {
      return this.urgency;
   }

   public LocalDateTime getTimeCreated() {
      return this.timeCreated;
   }

   public String getServiceStatus() {
      return this.serviceStatus;
   }

   public String getAssignedEmployee() {
      return assignedEmployee;
   }

   public void setServiceID(int serviceID) {
      this.serviceID = serviceID;
   }

   public void setRoomNumber(int roomNumber) {
      this.roomNumber = roomNumber;
   }

   public void setType(String type) {
      this.type = type;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public void setUrgency(String urgency) {
      this.urgency = urgency;
   }

   public void setTimeCreated(LocalDateTime timeCreated) {
      this.timeCreated = timeCreated;
   }

   public void setServiceStatus(String status) {
      this.serviceStatus = status;
   }

   public void setAssignedEmployee(String assignedEmployee) {
      this.assignedEmployee = assignedEmployee;
   }
}
