package com.hotelCheckIn;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    private Stage loginStage;
    
    @Override
    public void start(Stage stage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene (root);
            stage.setScene(scene);

            //Store reference to close later in case an admin wants to login
            loginStage = stage;

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

            //Admin Login; Shift + A + L
            scene.setOnKeyPressed(event -> {
                if (event.isShiftDown() && event.getCode() == KeyCode.A)
                {
                    scene.setOnKeyPressed(event2 -> {
                        if (event2.getCode() == KeyCode.L)
                        {
                            try {
                                adminLogin();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });

            stage.show();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //Close current fxml and load admin fxml file
    public void adminLogin() throws IOException
    {
        loginStage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("admin.fxml"));
        Parent adminRoot = loader.load();
        Scene adminScene = new Scene(adminRoot);
        Stage adminStage = new Stage();
        adminStage.setScene(adminScene);
        adminStage.setResizable(false);
        Image Icon = new Image("com/hotelCheckIn/images/cabin.png");
        adminStage.getIcons().add(Icon);
        adminStage.setTitle("Admin Login Panel");
        adminStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
