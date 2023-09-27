package demo.src.main.java.com.example;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene (root);
            stage.setScene(scene);

            //add CSS (extra - just in case)
            String css = this.getClass().getResource("style.css").toExternalForm();
            scene.getStylesheets().add(css);
            //

            //unresizable
            stage.setResizable(false);
            //

            //remove frame
            scene.setFill(Color.TRANSPARENT);
            stage.initStyle(StageStyle.TRANSPARENT);
            //

            //remove focus when the program starts
            root.requestFocus();
            //

            stage.show();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
