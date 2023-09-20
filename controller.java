import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class controller implements Initializable {
    
    @FXML
    private Text dragonsText;

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
    }

}