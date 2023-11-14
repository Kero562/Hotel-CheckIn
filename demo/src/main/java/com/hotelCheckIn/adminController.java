package com.hotelCheckIn;

import java.awt.TextField;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

public class adminController {

    
    @FXML
    private Button txtAdminPass;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passField;

    public void adminLogin()
    {
        controllerLogin(nameField.getText(), passField.getText());
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
