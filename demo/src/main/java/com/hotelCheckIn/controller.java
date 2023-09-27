package com.hotelCheckIn;
import java.lang.annotation.Native;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class controller implements Initializable {
    
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        textField1.pseudoClassStateChanged(empty, true);
        textField1.textProperty().addListener((obs, oldText, newText) -> {
        textField1.pseudoClassStateChanged(empty, newText.isEmpty());
        });

        textField2.pseudoClassStateChanged(empty, true);
        textField2.textProperty().addListener((obs, oldText, newText) -> {
        textField2.pseudoClassStateChanged(empty, newText.isEmpty());
        });
        //

        //Making Booking ID textfield numbers only
        textField2.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                // If the new input is not a digit, replace it with an empty string
                textField2.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        
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
        

        //Create gap (10 pixels) between exit and minimize buttons
        upperBox.setSpacing(10);

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
    }

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

    public void exitBtn()
    {
        Platform.exit();
    }

    public void minimizeFun()
    {
        Platform.runLater(() -> {
            Stage stage = (Stage) minimizeBtn.getScene().getWindow();
            stage.setIconified(true);
        });
    }
}