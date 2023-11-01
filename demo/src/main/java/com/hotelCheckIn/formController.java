package com.hotelCheckIn;

import java.time.LocalDate;

import org.apache.commons.validator.routines.EmailValidator;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

    @FXML
    private Button submitButton;

    @FXML
    private Label invalidEmailLabel;

    public void initialize() {

        //Connect label with its textfield
        emailLabel.setLabelFor(emailField);

        //Auto-set date-picker date to now
        datePicker.setValue(LocalDate.now());
    }

    public void formSubmit()
    {
        if(!EmailValidator.getInstance().isValid(emailField.getText()))
        {
            invalidEmailLabel.setVisible(true);
        }
    }
    
}
