package com.hotelCheckIn;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.mail.*;

public class requestsController {

    @FXML
    private VBox requestsBox;

    @FXML
    private ScrollPane scrollPane;

    //List of already added customer rows
    ArrayList<String> alreadyAdded = new ArrayList<>();
    
    public void initialize()
    {
        DatabaseUtil dbManager = new DatabaseUtil();
        //getting check-in logs
        String[] logs = dbManager.getLog();
        for (int i = 0; i < logs.length; i++)
        {
            boolean stop = false;
            if (logs[i] != null) {
                System.out.println(logs[i]);

                //The log is in A|B|C|D form, so split by |
                String[] split = logs[i].split("\\|");

                //just add the first customer, there wouldn't be an overlap since that's the first one. For the rest, check in the alreadyAdded to see if the customer is already there
                //if they are, fetch the accommodation info and append it to the already made row
                if (i == 0)
                {
                    alreadyAdded.add(split[0]);
                } else {
                    if (alreadyAdded.contains(split[0]))
                    {
                        for (Node node : requestsBox.getChildren())
                        {
                            if (node instanceof HBox)
                            {
                                HBox hbox = (HBox) node;
                                Label firstLabel = (Label) hbox.getChildren().get(0);
                                if (firstLabel.getText().equals(split[0]))
                                {
                                    Label services = (Label) hbox.getChildren().get(3);
                                    Label urgencyLabel = (Label) hbox.getChildren().get(4);
                                    services.setText(services.getText() + "/" + getAbbreviation(split[4]));
                                    urgencyLabel.setText(urgencyLabel.getText() + "/" + split[5]);
                                    stop = true;
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        alreadyAdded.add(split[0]);
                    }
                }

                //Don't add the duplicate customer row
                if (stop)
                {
                    stop = false;
                    continue;
                }

                HBox checkInBox = new HBox(70);
                checkInBox.setMinWidth(scrollPane.getWidth());
                checkInBox.setMaxHeight(100);
                checkInBox.setAlignment(Pos.CENTER);
                
                //add labels
                for (int j = 0; j < split.length; j++)
                {
                    Label label;
                    if (j == 3 || j == 6)
                    {
                        continue;
                    }
                    if (j == 4)
                    {
                        label = new Label(getAbbreviation(split[j]));
                    } else {
                        label = new Label(split[j]);
                    }
                    label.getStyleClass().add("logLabels");
                    checkInBox.getChildren().add(label);
                }

                //add buttons
                Button acptButton = new Button("Accept");
                Button rejectButton = new Button("Reject");

                acptButton.setOnAction(e -> {
                    dbManager.setServiceStatus(Integer.parseInt(split[2]), "Accepted");
                    Dialog<String> dialog = new Dialog<>();
                    dialog.setTitle("Enter Text");
                    dialog.setHeaderText(null);

                    TextArea textArea = new TextArea();
                    textArea.setPrefColumnCount(40);
                    textArea.setPrefRowCount(10);
                    textArea.setWrapText(true);

                    dialog.getDialogPane().setContent(textArea);

                    ButtonType confirmButtonType = new ButtonType("OK", ButtonData.OK_DONE);
                    dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

                    dialog.setResultConverter(buttonType -> {
                        if (buttonType == confirmButtonType)
                        {
                            return textArea.getText();
                        }
                        return null;
                    });

                    Optional<String> result = dialog.showAndWait();

                    result.ifPresent(text -> {
                        sendEmail(split[7], text, "Check In Approved");
                        requestsBox.getChildren().remove(checkInBox);
                    });
                });

                rejectButton.setOnAction(e -> {
                    dbManager.setServiceStatus(Integer.parseInt(split[2]), "Rejected");

                    Dialog<String> dialog = new Dialog<>();
                    dialog.setTitle("Enter Text");
                    dialog.setHeaderText(null);

                    TextArea textArea = new TextArea();
                    textArea.setPrefColumnCount(40);
                    textArea.setPrefRowCount(10);
                    textArea.setWrapText(true);

                    dialog.getDialogPane().setContent(textArea);

                    ButtonType confirmButtonType = new ButtonType("OK", ButtonData.OK_DONE);
                    dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

                    dialog.setResultConverter(buttonType -> {
                        if (buttonType == confirmButtonType)
                        {
                            return textArea.getText();
                        }
                        return null;
                    });

                    Optional<String> result = dialog.showAndWait();

                    result.ifPresent(text -> {
                        sendEmail(split[7], text, "Check In Disapproved");
                        requestsBox.getChildren().remove(checkInBox);
                    });
                });

                checkInBox.getChildren().add(acptButton);
                checkInBox.getChildren().add(rejectButton);

                requestsBox.getChildren().add(checkInBox);
            }
        }
    }

    public String getAbbreviation(String longString)
    {
        if (longString.equals("Digital Key Access")) return "DK";
        else if (longString.equals("Extra Mattress")) return "EM";
        else if (longString.equals("CheckOut Date")) return "CD";
        else return "-";
    }

    public void sendEmail(String theRecipient, String theMessage, String checkInDecision)
    {
        // email credentials and message data
        final String username = "4dragonsresort@gmail.com";
        final String password = "kbbd dmgx cqmg zewf";
        final String recipient = theRecipient;
        String subject = checkInDecision;
        String body = theMessage;

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
