import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
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

        //Remove bold from prompt texts
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

        //Make the pane as big as the screen

        //Booking ID numbers only
        textField2.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                // If the new input is not a digit, replace it with an empty string
                textField2.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        
        
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        myPane.setMinWidth(bounds.getWidth());
        myPane.setMinHeight(bounds.getHeight());

        myPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
        
        draggableMaker.makeDraggable(myPane);
    }
}