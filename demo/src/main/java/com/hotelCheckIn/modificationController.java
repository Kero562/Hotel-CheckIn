package com.hotelCheckIn;

import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class modificationController {

    @FXML
    private Button submitButton;

    @FXML
    private ToggleGroup extraBed;

    @FXML
    private ToggleGroup digitalKey;

    @FXML
    private RadioButton yesButton;

    @FXML
    private RadioButton yesButton1;

    @FXML
    private RadioButton noButton;

    @FXML
    private RadioButton noButton1;

    @FXML
    private RadioButton yesbutton1;

    @FXML
    private ImageView mattressView;

    @FXML
    private ImageView keyView;

    @FXML
    private ImageView checkOutView;

    @FXML
    private AnchorPane mainBackground;

    @FXML
    private AnchorPane optionsPane;

    @FXML
    private DatePicker dateModifier;

    RadioButton[] mattressButtons;
    RadioButton[] keyButtons;

    public void initialize()
    {
        mattressButtons = new RadioButton[2];
        mattressButtons[0] = yesButton;
        mattressButtons[1] = noButton;

        keyButtons = new RadioButton[2];
        keyButtons[0] = yesButton1;
        keyButtons[1] = noButton1;
    }
    
    //Functional interface with function defined in formController
    public interface SubmitEventHandler {
        void onSubmit(JSONObject json);
    }

    private SubmitEventHandler submitEventHandler;

    public void setSubmitEventHandler(SubmitEventHandler handler)
    {
        this.submitEventHandler = handler;
    }

    public void submit()
    {
        JSONObject json = new JSONObject();

        if ((RadioButton) extraBed.getSelectedToggle() == noButton)
        {
            json.put("bedChoice", 0);
        } else {
            json.put("bedChoice", 1);
        }

        if ((RadioButton) digitalKey.getSelectedToggle() == noButton1)
        {
            json.put("digitalKeyChoice", 0);
        } else {
            json.put("digitalKeyChoice", 1);
        }

        if (submitEventHandler != null)
        {
            submitEventHandler.onSubmit(json);
        }

        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void mattressClick()
    {
        Image image = new Image("com/hotelCheckIn/images/clickMattress.png");
        mattressView.setImage(image);

        //ensure elements from each service don't collide
        for (int i = 0; i < keyButtons.length; i++)
        {
            keyButtons[i].setVisible(false);
        }
        dateModifier.setVisible(false);
        //

        optionsPane.setVisible(true);
        yesButton.setVisible(true);
        noButton.setVisible(true);
    }

    public void keyClick()
    {
        Image image = new Image("com/hotelCheckIn/images/clickKey.png");
        keyView.setImage(image);

        //ensure elements from each service don't collide
        for (int i = 0; i < mattressButtons.length; i++)
        {
            mattressButtons[i].setVisible(false);
        }
        dateModifier.setVisible(false);
        //

        optionsPane.setVisible(true);
        yesButton1.setVisible(true);
        noButton1.setVisible(true);
    }

    public void checkOutClick()
    {
        Image image = new Image("com/hotelCheckIn/images/clickCheckout.png");
        checkOutView.setImage(image);

        //ensure elements from each service don't collide
        for (int i = 0; i < mattressButtons.length; i++)
        {
            mattressButtons[i].setVisible(false);
        }
        
        for (int i = 0; i < keyButtons.length; i++)
        {
            keyButtons[i].setVisible(false);
        }
        //

        optionsPane.setVisible(true);
        dateModifier.setVisible(true);
    }

    public void sceneClicked(MouseEvent event)
    {
        if (!mattressView.getBoundsInParent().contains(event.getX(), event.getY())) {
            Image initialImage = new Image("com/hotelCheckIn/images/mattress.png");
            mattressView.setImage(initialImage);
        }

        if (!keyView.getBoundsInParent().contains(event.getX(), event.getY()))
        {
            Image initialImage = new Image("com/hotelCheckIn/images/digitalKey.png");
            keyView.setImage(initialImage);
        }

        if (!checkOutView.getBoundsInParent().contains(event.getX(), event.getY()))
        {
            Image initialImage = new Image("com/hotelCheckIn/images/checkout.png");
            checkOutView.setImage(initialImage);
        }
    }
}
