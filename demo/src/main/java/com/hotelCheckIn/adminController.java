package com.hotelCheckIn;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class adminController {

    
    @FXML
    private Button txtAdminPass;

    public void adminLogin()
    {
        controllerLogin(null, null);
    }

    public void controllerLogin(String username, String password) {

        DatabaseUtil dbManager = new DatabaseUtil();
        if(dbManager.isAdmin(username, password))
        {
            System.out.println("Admin login successful");
        }
        else
        {
            System.out.println("Admin login failed");
        }
    }
}
