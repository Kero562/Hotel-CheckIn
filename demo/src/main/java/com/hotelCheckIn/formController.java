package com.hotelCheckIn;

import java.time.LocalDate;
import java.util.Locale;

import org.apache.commons.validator.routines.EmailValidator;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.input.KeyEvent;
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

    @FXML
    private TextField phoneField;

    @FXML
    private Label invalidNumLabel;

    String[] locales = Locale.getISOCountries();

    @FXML
    private ComboBox<String> cCodeCB;

    private int[] lastSelectedIndex = new int[26];

    @FXML
    private Label countryCodeLabel;

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
        }

        //Phone number formatting for phoneField
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                // If the new input is not a digit, replace it with an empty string
                phoneField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        //Country-code comboBox
        ObservableList<String> options = FXCollections.observableArrayList(locales);
        cCodeCB.setItems(options);
        cCodeCB.addEventFilter(KeyEvent.KEY_PRESSED, event -> manualMnemonic(event));
    }
    
    public void manualMnemonic(KeyEvent event)
    {
        if (event.getCode().isLetterKey()) {
            String pressedKey = event.getText().toUpperCase();

            int currentIndex = cCodeCB.getSelectionModel().getSelectedIndex();
            int startIndex = lastSelectedIndex[pressedKey.charAt(0) - 'A'];

            // Iterate over items to find the next one starting with the pressed key
            for (int i = startIndex + 1; i < cCodeCB.getItems().size(); i++) {
                String item = cCodeCB.getItems().get(i);
                if (item.toUpperCase().startsWith(pressedKey)) {
                    cCodeCB.getSelectionModel().select(item);
                    //Scroll to selected item
                    ComboBoxListViewSkin<?> skin = (ComboBoxListViewSkin<?>) cCodeCB.getSkin();
                    ListView<?> list = (ListView<?>) skin.getPopupContent();
                    list.scrollTo(i);
                    //
                    lastSelectedIndex[pressedKey.charAt(0) - 'A'] = i;
                    event.consume();
                    break;
                }
            }

            // If not found after the current index, start from the beginning
            if (currentIndex == cCodeCB.getSelectionModel().getSelectedIndex()) {
                for (int i = 0; i <= startIndex; i++) {
                    String item = cCodeCB.getItems().get(i);
                    if (item.toUpperCase().startsWith(pressedKey)) {
                        cCodeCB.getSelectionModel().select(item);
                        //same as above
                        ComboBoxListViewSkin<?> skin = (ComboBoxListViewSkin<?>) cCodeCB.getSkin();
                        ListView<?> list = (ListView<?>) skin.getPopupContent();
                        list.scrollTo(i);
                        //
                        lastSelectedIndex[pressedKey.charAt(0) - 'A'] = i;
                        event.consume();
                        break;
                    }
                }
            }
        }
    }

    public void formSubmit() throws NumberParseException
    {
        String countryCode = cCodeCB.getSelectionModel().getSelectedItem();
        String digits = phoneField.getText();

        //reset before checking
        invalidEmailLabel.setVisible(false);
        invalidNumLabel.setVisible(false);
        countryCodeLabel.setVisible(false);
        //

        boolean formComplete = true;

        //Check email validity
        if(!EmailValidator.getInstance().isValid(emailField.getText()))
        {
            invalidEmailLabel.setVisible(true);
            Toolkit.getDefaultToolkit().beep();
            formComplete = false;
        }
        //

        //Check Phone Number validity
        if (digits != null && countryCode != null)
        {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            PhoneNumber number = phoneUtil.parse(digits, countryCode);

            if (!phoneUtil.isValidNumber(number))
            {
            invalidNumLabel.setVisible(true);
            Toolkit.getDefaultToolkit().beep();
            formComplete = false;
            }
        } else {
            if (digits == null)
            {
                invalidNumLabel.setVisible(true);
                Toolkit.getDefaultToolkit().beep();
                formComplete = false;
            } else
            {
                countryCodeLabel.setVisible(true);
                Toolkit.getDefaultToolkit().beep();
                formComplete = false;
            }
        }
        //
    }
    
}
