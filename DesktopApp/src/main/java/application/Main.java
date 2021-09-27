package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application{

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/content/loading-scene.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        /*
        CSS:

        String font, group, padding;
        font = this.getClass().getResource("/styles/font.css").toExternalForm();
        group = this.getClass().getResource("/styles/group.css").toExternalForm();
        padding = this.getClass().getResource("/styles/padding.css").toExternalForm();
        scene.getStylesheets().add(font);
        //stage.setTitle("Hello!");

        */
    }

    public static void main(String[] args) {
        launch();
    }
}
