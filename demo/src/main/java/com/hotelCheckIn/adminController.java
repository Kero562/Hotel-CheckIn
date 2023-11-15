package com.hotelCheckIn;


import java.io.IOException;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.Toolkit;

public class adminController {

    
    @FXML
    private Button txtAdminPass;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passField;

    public void adminLogin() throws IOException
    {
        Stage currentStage = (Stage) nameField.getScene().getWindow();
        if (controllerLogin(nameField.getText(), passField.getText()) == true)
        {
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("requests.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Requests List");
            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.setResizable(false);
            stage.show();
        } else {
            Toolkit.getDefaultToolkit().beep();
            shakeStage(currentStage);
        }
    }

    private void shakeStage(Stage stage)
    {
        double originalX = stage.getX();
        int cycleCount = 1; // Number of shakes

        DoubleProperty xProperty = new SimpleDoubleProperty(originalX);
        xProperty.addListener((observable, oldValue, newValue) -> stage.setX(newValue.doubleValue()));

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.05), new KeyValue(xProperty, originalX - 10)),
                new KeyFrame(Duration.seconds(0.1), new KeyValue(xProperty, originalX + 10)),
                new KeyFrame(Duration.seconds(0.15), new KeyValue(xProperty, originalX - 10)),
                new KeyFrame(Duration.seconds(0.2), new KeyValue(xProperty, originalX + 10)),
                new KeyFrame(Duration.seconds(0.25), new KeyValue(xProperty, originalX - 10)),
                new KeyFrame(Duration.seconds(0.3), new KeyValue(xProperty, originalX + 10)),
                new KeyFrame(Duration.seconds(0.35), new KeyValue(xProperty, originalX - 10)),
                new KeyFrame(Duration.seconds(0.4), new KeyValue(xProperty, originalX + 10)),
                new KeyFrame(Duration.seconds(0.45), new KeyValue(xProperty, originalX - 10)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(xProperty, originalX + 10))
        );
        timeline.setCycleCount(cycleCount);
        timeline.setOnFinished(event -> stage.setX(originalX));
        timeline.play();
    }

    private boolean controllerLogin(String username, String password) {

        DatabaseUtil dbManager = new DatabaseUtil();
        if(dbManager.isAdmin(username, password))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
