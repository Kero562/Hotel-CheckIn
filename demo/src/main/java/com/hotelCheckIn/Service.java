package com.hotelCheckIn;

public class Service {
   private int serviceID;
   private String urgency;

   Service(int serviceID, String urgency) {
      this.serviceID = serviceID;
      this.urgency = urgency;
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
