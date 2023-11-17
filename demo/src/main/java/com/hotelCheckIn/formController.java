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
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

    private byte serviceExtraBed = 0;
    private byte serviceDigitalKey = 0;

    @FXML
    private VBox serviceBox;

    private boolean bedLabelAdded = false;
    private boolean keyLabelAdded = false;

    private int customerID;

    @FXML
    private TextField roomField;

    //For service scene preservation across invokes
    private Stage serviceStage;
    private Scene serviceScene;

    //Horizontal boxes added within the services vertical box
    HBox bedBox;
    HBox keyBox;

    ObservableList<String> urgencyList = FXCollections.observableArrayList("Select Urgency", "Low", "Normal", "High");

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

        //Number formatting for room number (max 3 digits)
        roomField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,3}")) {
                roomField.setText(oldValue);
            }
        });
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

        //When form is complete, make the request
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

        //Form completion
        if (formComplete)
        {
            DatabaseUtil dbManager = new DatabaseUtil();
            int roomNumber = Integer.parseInt(roomField.getText());
            dbManager.checkIn(customerID, roomNumber);
        }
    }

    //Open services fxml
    public void serviceOpen()
    {
        if (serviceStage == null)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modificationsPage.fxml"));
            Parent root = loader.load();

            serviceStage = new Stage();
            serviceStage.setTitle("Services");
            //Make it a child window to this
            serviceStage.initModality(Modality.APPLICATION_MODAL);
            serviceStage.initOwner(submitButton.getScene().getWindow());
            //
            serviceScene = new Scene(root);
            serviceStage.setScene(serviceScene);

            //Send info back to parent controller here upon submission
            modificationController childController = loader.getController();
            childController.setSubmitEventHandler(json -> {

                serviceExtraBed = (byte) json.getInt("bedChoice");
                serviceDigitalKey = (byte) json.getInt("digitalKeyChoice");

                //Add/remove services (HBoxes with components inside) accordingly
                manageServiceBox();
            });

            serviceStage.setResizable(false);
            serviceStage.showAndWait();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    } else {
        serviceStage.showAndWait();
    }
}

    private void manageServiceBox()
    {
        if (serviceExtraBed == 1 && !bedLabelAdded)
        {
            bedBox = new HBox(6);
            bedBox.setAlignment(Pos.CENTER);
            Label bedLabel = new Label("- Extra Bed");
            bedLabel.getStyleClass().add("serviceLabels");
            serviceBox.getChildren().add(bedBox);
            bedBox.getChildren().add(bedLabel);

            ChoiceBox<String> urgencyBox = new ChoiceBox<>(urgencyList);
            handleBoxSelection(urgencyBox);

            bedBox.getChildren().add(urgencyBox);

            bedLabelAdded = true;
        } else if (serviceExtraBed == 0 && bedLabelAdded)
        {
            serviceBox.getChildren().remove(bedBox);
            bedLabelAdded = false;
        }

        if (serviceDigitalKey == 1 && !keyLabelAdded)
        {
            keyBox = new HBox(6);
            keyBox.setAlignment(Pos.CENTER);
            Label keyLabel = new Label("- Digital Key Access");
            keyLabel.getStyleClass().add("serviceLabels");
            serviceBox.getChildren().add(keyBox);
            keyBox.getChildren().add(keyLabel);

            ChoiceBox<String> urgencyBox = new ChoiceBox<>(urgencyList);
            handleBoxSelection(urgencyBox);

            keyBox.getChildren().add(urgencyBox);

            keyLabelAdded = true;
        } else if (serviceDigitalKey == 0 && keyLabelAdded)
        {
            serviceBox.getChildren().remove(keyBox);
            keyLabelAdded = false;
        }
    }

    public void passCustomerID(String id)
    {
        customerID = Integer.parseInt(id);
    }

    private boolean selectionCancelled = false;
    private void handleBoxSelection(ChoiceBox<String> box)
    {
        box.getSelectionModel().selectFirst();

        box.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Select Urgency".equals(newValue) && !selectionCancelled)
            {
                box.getSelectionModel().select(oldValue);
            }
            else if (!"Select Urgency".equals(newValue)){
                urgencyList.remove("Select Urgency");
            }
        });

        box.setOnHidden(event -> {
            if (box.getSelectionModel().getSelectedIndex() == 0)
            {
                selectionCancelled = true;
                box.getSelectionModel().select(0);
            }
        });

        box.setOnShown(event -> {
            selectionCancelled = false;
        });
    }
}
