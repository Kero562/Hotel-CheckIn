package com.hotelCheckIn;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class formController{

    @FXML
    private Label emailLabel;

    @FXML
    private TextField emailField;

    @FXML
    private DatePicker datePicker;

    public void initialize() {

        //Connect label with its textfield
        emailLabel.setLabelFor(emailField);

        //Auto-set date-picker date to now
        datePicker.setValue(LocalDate.now());
    }
    
}
