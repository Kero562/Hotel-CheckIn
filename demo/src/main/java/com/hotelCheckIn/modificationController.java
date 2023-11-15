package com.hotelCheckIn;

import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
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
    private RadioButton noButton;

    @FXML
    private RadioButton noButton1;

    @FXML
    private RadioButton yesbutton1;

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

    public void preSetOptions(byte bed, byte key)
    {
        System.out.println(bed);
        System.out.println(key);

        if (bed == 1)
        {
            extraBed.selectToggle(yesButton);
        }

        if (key == 1)
        {
            digitalKey.selectToggle(yesbutton1);
        }
    }
}
