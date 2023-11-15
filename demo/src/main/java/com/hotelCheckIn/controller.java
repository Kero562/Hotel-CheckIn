package com.hotelCheckIn;

import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class controller {
    
    @FXML
    private Text dragonsText;

    @FXML
    private TextField textField1;

    @FXML
    private TextField textField2;

    @FXML
    private Button checkInButton;

    @FXML
    private Pane myPane;

    DraggableMaker draggableMaker = new DraggableMaker();

    @FXML
    private Button exitBtn;

    @FXML
    private HBox upperBox;

    @FXML
    private Button minimizeBtn;

    @FXML
    private Label completionText;

    @FXML
    private VBox formBox;

    //Hover effect for check-in button (set on lines 160-161)
    ScaleTransition trans = new ScaleTransition();

    public void initialize() {
        
        //Ensure font applies for dragon text - font already downloaded
        Font dragonFont = Font.loadFont(getClass().getResourceAsStream("/com/hotelCheckIn/fonts/euphorigenic.otf"), 42);
        dragonsText.setFont(dragonFont);


        //Writing animation

        String textToAnimate = "4 Dragons Resort";

        int durationPerCharacter = 100;

        Timeline timeline = new Timeline();
        for (int i = 0; i <= textToAnimate.length(); i++) {
            int finalI = i;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(i * durationPerCharacter), event -> {
                dragonsText.setText(textToAnimate.substring(0, finalI));
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.play();
        //

        //Remove bold (and any style applied to text) from prompt texts - Check CSS empty subclass
        PseudoClass empty = PseudoClass.getPseudoClass("empty");
        textField1.pseudoClassStateChanged(empty, true); //basically means apply "empty" to textField1
        textField1.textProperty().addListener((obs, oldText, newText) -> {
        textField1.pseudoClassStateChanged(empty, newText.isEmpty()); //if nothing is written in the textfields (newText is empty), then apply the "empty" CSS class
        });

        textField2.pseudoClassStateChanged(empty, true);
        textField2.textProperty().addListener((obs, oldText, newText) -> {
        textField2.pseudoClassStateChanged(empty, newText.isEmpty());
        });
        //

        /*
        //Making Booking ID textfield numbers only
        textField2.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                // If the new input is not a digit, replace it with an empty string
                textField2.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        */
        
        //make transparent pane fill the screen for dragging
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        myPane.setMinWidth(bounds.getWidth());
        myPane.setMinHeight(bounds.getHeight());

        myPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
        
        draggableMaker.makeDraggable(myPane);
        //

        //Exit button
        
        ImageView icon = new ImageView("com/hotelCheckIn/images/cancel.png");
        icon.setFitHeight(30);
        icon.setFitHeight(30);
        icon.setPreserveRatio(true);
        exitBtn.getStyleClass().add("exitBtn");
        exitBtn.setGraphic(icon);
        
        //

        //Minimize Button
        
        ImageView icon2 = new ImageView("com/hotelCheckIn/images/min.png");
        icon2.setFitHeight(24);
        icon2.setFitHeight(24);
        icon2.setPreserveRatio(true);
        minimizeBtn.getStyleClass().add("exitBtn");
        minimizeBtn.setGraphic(icon2);
        //

        //Create gap (10 pixels) between exit and minimize buttons
        upperBox.setSpacing(10);
        //

        //Take focus away from anything focusable when clicked away (textField1 here is used as focus point but anything else can be used too)
        Platform.runLater(() -> {
            myPane.getScene().setOnMousePressed(event -> {
            if (!textField1.equals(event.getSource()))
            {
                textField1.getParent().requestFocus();
            }
        });
        });
        //

        //Initialize check-in hover effect for later use
        trans.setDuration(Duration.seconds(0.6));
        trans.setNode(checkInButton);
        //
    }

    //Hover effect on for exit and minimize buttons
    public void hoverEffectOn(MouseEvent event)
    {
        Button sourceBtn = (Button) event.getSource();

        if (sourceBtn == exitBtn)
        {
            exitBtn.setStyle(null);
            exitBtn.getStyleClass().add("exitHover");
        } else if (sourceBtn == minimizeBtn)
        {
            minimizeBtn.setStyle(null);
            minimizeBtn.getStyleClass().add("exitHover");
        }
    }

    //Hover effect off for exit and minimize buttons
    public void hoverEffectOff(MouseEvent event)
    {
        Button sourceBtn = (Button) event.getSource();

        if (sourceBtn == exitBtn)
        {
            exitBtn.getStyleClass().remove("exitHover");
            exitBtn.getStyleClass().add("exitBtn");
        } else if (sourceBtn == minimizeBtn)
        {
            minimizeBtn.getStyleClass().remove("exitHover");
            minimizeBtn.getStyleClass().add("exitBtn");
        }
    }

    //Exit button functionality
    public void exitBtn()
    {
        Platform.exit();
    }

    //Minimize functionality -- TBA: minimize using windows animation using JNA library
    public void minimizeFun()
    {
        Platform.runLater(() -> {
            Stage stage = (Stage) minimizeBtn.getScene().getWindow();
            stage.setIconified(true);
        });
    }

    //Hover on for check-in button
    public void checkHoverOn()
    {
        //change cursor to hand
        checkInButton.getStyleClass().add("checkStyleOn");

        //increase size transition
        trans.setToX(1.2);
        trans.setToY(1.2);

        //play
        trans.play();
    }


    //Hover off for check-in button
    public void checkHoverOff()
    {
        //stop the increase size transition
        trans.stop();

        //change cursor back to normal
        checkInButton.getStyleClass().remove("checkStyleOn");

        //reduce size transition
        ScaleTransition resetTrans = new ScaleTransition(Duration.seconds(0.6), checkInButton);
        resetTrans.setToX(1.0);
        resetTrans.setToY(1.0);
        resetTrans.play();
    }

    //Press effect for check-in button
    public void checkPressOn()
    {
        checkInButton.setStyle("-fx-background-color: rgb(6, 0, 56);");
    }

    //release press effect for the check-in button (return color to normal)
    public void checkPressOff()
    {
        checkInButton.setStyle("-fx-background-color: #3653F8");
    }

    public void checkWindow()
    {
        DatabaseUtil dbManager = new DatabaseUtil();
        //Temporary Verification
        // Amend this to check the status
        if (dbManager.isValidReservation(textField1.getText(), textField2.getText()))
        {
            // Check if the user is checked in or not here with nested if using the code below and tell the user what the status is
            completionText.setStyle("-fx-text-fill: red;");
            completionText.setText("Loading Failed - No booking found");
        }

        else {
            Stage currentStage = (Stage) checkInButton.getScene().getWindow();
            Scene scene = checkInButton.getScene();

            scene.setCursor(Cursor.WAIT);
            checkInButton.setDisable(true);
            completionText.setText("Loaded Successfully");
            
            Duration delay = Duration.seconds(2);
            KeyFrame keyFrame = new KeyFrame(delay, event -> {
            
            currentStage.close();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("form.fxml"));
                Parent root = loader.load();

                Stage newStage = new Stage();
                newStage.setTitle("Form");

                Scene newScene = new Scene(root);
                newStage.setScene(newScene);

                //send customer ID to form controller
                formController controller = loader.getController();
                controller.passCustomerID(textField2.getText());

                newStage.setResizable(false);
                newStage.show();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            });
            Timeline timeline = new Timeline(keyFrame);
            timeline.play();
        }
        //
    }
}