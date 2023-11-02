package com.hotelCheckIn;

import java.time.LocalDate;
import org.apache.commons.validator.routines.EmailValidator;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.awt.Toolkit;
import java.io.File;
import java.net.MalformedURLException;

public class formController {

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

    @FXML
    private WebView webView;

    private WebEngine engine;

    public void initialize() {

        //Connect label with its textfield
        emailLabel.setLabelFor(emailField);

        //Auto-set date-picker date to now
        datePicker.setValue(LocalDate.now());

        //Set engine and set webView background transparent
        engine = webView.getEngine();
        final com.sun.webkit.WebPage webPage = com.sun.javafx.webkit.Accessor.getPageFor(engine);
        webPage.setBackgroundColor(0);

        //Open webView's html
        File file = new File("demo\\src\\main\\java\\com\\hotelCheckIn\\index.html");

        try {
            engine.load(file.toURI().toURL().toString());
        } catch (MalformedURLException e) {
            engine.load("<h1>File not found</h1>");
            System.out.println("didn't load");
        }
    }

    public void formSubmit()
    {
        if(!EmailValidator.getInstance().isValid(emailField.getText()))
        {
            invalidEmailLabel.setVisible(true);
            Toolkit.getDefaultToolkit().beep();
        }
    }
    
}
