package com.hotelCheckIn;

import java.time.LocalDateTime;

public class Service {
   private String type;
   private int priority;
   private LocalDateTime timeCreated;
   private String assignedEmployee;

   public String getType() {
      return type;
   }

   public int getPriority() {
      return priority;
   }

   public LocalDateTime getTimeCreated() {
      return timeCreated;
   }

   public String getAssignedEmployee() {
      return assignedEmployee;
   }

   public void setType(String type) {
      this.type = type;
   }

   public void setPriority(int priority) {
      this.priority = priority;
   }

   public void setTimeCreated(LocalDateTime timeCreated) {
      this.timeCreated = timeCreated;
   }

   public void setAssignedEmployee(String assignedEmployee) {
      this.assignedEmployee = assignedEmployee;
   }
}
