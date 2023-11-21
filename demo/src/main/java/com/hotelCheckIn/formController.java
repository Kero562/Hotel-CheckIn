package com.hotelCheckIn;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Locale;

import org.apache.commons.validator.routines.EmailValidator;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

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

    //Service variables
    private byte serviceExtraBed = 0;
    private byte serviceDigitalKey = 0;
    private String serviceNewCheckout;

    @FXML
    private VBox serviceBox;

    private boolean bedLabelAdded = false;
    private boolean keyLabelAdded = false;
    private boolean newCheckOutAdded = false;

    private Customer customer;

    @FXML
    private TextField roomField;

    //For service scene preservation across invokes
    private Stage serviceStage;
    private Scene serviceScene;

    //Horizontal boxes added within the services vertical box
    HBox bedBox;
    HBox keyBox;

    ObservableList<String> urgencyList = FXCollections.observableArrayList("Select Urgency", "Low", "Normal", "High");

    //For console and its log function (console newly created member) -- mimics the console.log JS but not actually it
    ConsoleBridge conBridge = new ConsoleBridge();

    //For javafxInterface and its receiveClassName function (javafxInterface newly created member) -- passing the controller so I can change the Label within the class
    JavaBridge javaBridge = new JavaBridge(this);

    @FXML
    private Label typeConfLabel;

    private boolean selectionCancelled = false;

    String customerRoomType;

    @FXML
    private Label typeChange;

    ChoiceBox<String> BedUrgencyBox;
    ChoiceBox<String> KeyUrgencyBox;

    @FXML
    private Label servicesLabel;

    public void initialize() {

        //Set engine and set webView background transparent
        engine = webView.getEngine();
        final com.sun.webkit.WebPage webPage = com.sun.javafx.webkit.Accessor.getPageFor(engine);
        webPage.setBackgroundColor(0);

        //Open webView's html
        File file = new File("demo\\src\\main\\java\\com\\hotelCheckIn\\index.html");

        try {
            engine.load(file.toURI().toURL().toString());
            engine.setJavaScriptEnabled(true);
            engine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED)
                {
                    JSObject window = (JSObject) engine.executeScript("window");
                    window.setMember("javafxInterface", javaBridge);
                    window.setMember("console", conBridge);
                }
            });
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

        //Set roomField & datePicker uneditable
        roomField.setEditable(false);
        datePicker.setEditable(false);
    }

    //Either one can be used -- Keeping both to show that you can create your own "function" (javafxInterface) to bridge from JS
    //Controller passing explained at line 109
    public static class JavaBridge {
        private formController controller;

        JavaBridge(formController controller)
        {
            this.controller = controller;
        }
        public void receiveID(String className)
        {
            controller.typeConfLabel.setText(className);
            if (!controller.customerRoomType.equals(className))
            {
                controller.typeChange.setVisible(true);
                controller.roomField.setDisable(true);
            } else {
                controller.typeChange.setVisible(false);
                controller.roomField.setDisable(false);
            }
        }
    }

    public class ConsoleBridge {
        public void log(String message)
        {
            System.out.println(message);
        }
    }
    //

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
        servicesLabel.setVisible(false);
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
        if (!digits.isEmpty() && countryCode != null)
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
            if (digits.isEmpty())
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

        //Check urgency
        if (BedUrgencyBox != null)
        {
            if (BedUrgencyBox.getSelectionModel().getSelectedItem().equals("Select Urgency"))
            {
                servicesLabel.setVisible(true);
                formComplete = false;
            }
        }

        if (KeyUrgencyBox != null)
        {
            if (KeyUrgencyBox.getSelectionModel().getSelectedItem().equals("Select Urgency"))
            {
                servicesLabel.setVisible(true);
                formComplete = false;
            }
        }

        //Form completion
        if (formComplete)
        {
            
            DatabaseUtil dbManager = new DatabaseUtil();
            int roomNumber = Integer.parseInt(roomField.getText());
            dbManager.checkIn(customer.getCustomerID(), roomNumber);
            

            //Close stage
            Stage theStage = (Stage) submitButton.getScene().getWindow();
            theStage.close();

            customer.setEmailAddress(emailField.getText());
            customer.setPhoneNumber(Long.parseLong(phoneField.getText()));

            if (roomField.isDisabled())
            {
                //customer.getReservations().get(0).setReservationStatus("Changed");
            } else {
                customer.getReservations().get(0).setReservationStatus("Checked In");
            }

            //Set confirmation alert
            Alert alert = new Alert(AlertType.NONE);
            alert.setTitle("Form submitted");
            alert.setHeaderText(null);
            alert.setContentText("Form was submitted successfully!");

            alert.getButtonTypes().add(ButtonType.OK);
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(new Image("com/hotelCheckIn/images/cabin.png"));

            alert.showAndWait();
            //

            if (bedLabelAdded) {
                Service bedService = new Service(roomNumber, "Extra Mattress", BedUrgencyBox.getSelectionModel().getSelectedItem());
            }
            if (keyLabelAdded) {
                Service keyService = new Service(roomNumber, "Digital Key Access", KeyUrgencyBox.getSelectionModel().getSelectedItem());
            }
            if (newCheckOutAdded) {
                Service checkOutService = new Service(roomNumber, "CheckOut Date", "Normal");
            }

            if (!bedLabelAdded && !keyLabelAdded && !newCheckOutAdded)
            {
                Service service = new Service(roomNumber, "N/A", "N/A");
            }
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
                if (json.has("checkOut"))
                {
                    serviceNewCheckout = json.getString("checkOut");
                }

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
            Label bedLabel = new Label("- Extra Mattress");
            bedLabel.getStyleClass().add("serviceLabels");
            serviceBox.getChildren().add(bedBox);
            bedBox.getChildren().add(bedLabel);

            BedUrgencyBox = new ChoiceBox<>(urgencyList);
            handleBoxSelection(BedUrgencyBox);

            bedBox.getChildren().add(BedUrgencyBox);

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

            KeyUrgencyBox = new ChoiceBox<>(urgencyList);
            handleBoxSelection(KeyUrgencyBox);

            keyBox.getChildren().add(KeyUrgencyBox);

            keyLabelAdded = true;
        } else if (serviceDigitalKey == 0 && keyLabelAdded)
        {
            serviceBox.getChildren().remove(keyBox);
            keyLabelAdded = false;
        }

        //Checkout service
        Long checkOutEpoch = customer.getReservations().get(0).getCheckOut();
        LocalDate customerCheckOutDate = Instant.ofEpochMilli(checkOutEpoch).atZone(ZoneId.systemDefault()).toLocalDate();
        Label checkOutLabel = new Label();
        checkOutLabel.setId("checkOutLabel");
        LocalDate checkOutDateFormat = (serviceNewCheckout != null) ? LocalDate.parse(serviceNewCheckout) : null;
        HBox checkOutBox = new HBox();
        checkOutBox.setId("checkOutBox");

        //If it's the same date
        if (serviceNewCheckout != null && checkOutDateFormat.equals(customerCheckOutDate))
        {
            //Remove existing checkOut date if any
            if (newCheckOutAdded)
            {
                HBox actualCheckOutBox = (HBox) serviceBox.lookup("#checkOutBox");
                serviceBox.getChildren().remove(actualCheckOutBox);
                newCheckOutAdded = false;
            }
        } else {
            //If new checkout date is requested with no previously added service, add it
            if (serviceNewCheckout != null && !newCheckOutAdded)
            {
                checkOutBox.setAlignment(Pos.CENTER);
                if (checkOutDateFormat.isBefore(customerCheckOutDate))
                {
                    checkOutLabel.setText("- Earlier CheckOut Date: " + checkOutDateFormat.toString());
                } else
                {
                    checkOutLabel.setText("- Later CheckOut Date: " + checkOutDateFormat.toString());
                }
                checkOutLabel.getStyleClass().add("serviceLabels");
                serviceBox.getChildren().add(checkOutBox);
                checkOutBox.getChildren().add(checkOutLabel);

                newCheckOutAdded = true;
            }
            //update the checkout date if changed 
            else if (serviceNewCheckout != null && newCheckOutAdded)
            {
                HBox actualCheckOutBox = (HBox) serviceBox.lookup("#checkOutBox");
                Label actualCheckOutLabel = (Label) actualCheckOutBox.lookup("#checkOutLabel");
                if (checkOutDateFormat.isBefore(customerCheckOutDate))
                {
                actualCheckOutLabel.setText("- Earlier CheckOut Date: " + checkOutDateFormat.toString());
                } else
                {
                    actualCheckOutLabel.setText("- Later CheckOut Date: " + checkOutDateFormat.toString());
                }
            }
        }
        //
    }

    //Acts as a second initialize but after the actual one
    public void passCustomer(Customer customer)
    {
        this.customer = customer;
        Reservation customerReservation = customer.getReservations().get(0);
        
        //Convert the checkIn date from epoch milliseconds to LocalDate and put it on datePicker
        Long epoch = customer.getReservations().get(0).getCheckIn();

        LocalDate fromMill = Instant.ofEpochMilli(epoch).atZone(ZoneId.systemDefault()).toLocalDate();
        datePicker.setValue(fromMill);
        //

        //Set room to the customer's room
        roomField.setText(Integer.toString(customerReservation.getRoom().getRoomNumber()));
        //

        //Set room type to the customer's room type
        customerRoomType = customerReservation.getRoom().getRoomType();
        
        switch (customerRoomType) {
            case "SingleRoom":
                customerRoomType = "Single";
                typeConfLabel.setText(customerRoomType);
                break;
            case "DoubleRoom":
                customerRoomType = "Double";
                typeConfLabel.setText(customerRoomType);
                break;
            default:
                customerRoomType = "Twin";
                typeConfLabel.setText(customerRoomType);
                break;
        }
        //
    }

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