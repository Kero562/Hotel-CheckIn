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

// importing libraries used for java mail
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

import java.awt.Toolkit;

public class adminController {

    
    @FXML
    private Button txtAdminPass;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passField;

    //Upon successful login, load the requests page. Otherwise shake the screen and give an error (beep) sound
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

    //Shake effect
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

    public void sendEmail()
    {
        // email credentials and message data
        final String username = "4dragonsresort@gmail.com";
        final String password = "kbbd dmgx cqmg zewf";
        final String recipient = "kerolosharby@gmail.com";
        String subject = "Hello world!";
        String body = "This is a test message from Java mail. If you see this then it's working.";

        // add mail server/encryption properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        // create a new email session
        Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		};
        Session session = Session.getInstance(properties, auth);

        // send the email
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("adminController.sendEmail(): Email sent sucessfully!");
        } catch(MessagingException e) {
            System.out.println("adminController.sendEmail(): MessagingException");
            e.printStackTrace();
        }
    }

}
