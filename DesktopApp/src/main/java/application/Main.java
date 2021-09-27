package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application{

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/content/loading-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        String font, group, padding;
        font = this.getClass().getResource("/styles/font.css").toExternalForm();
        group = this.getClass().getResource("/styles/group.css").toExternalForm();
        padding = this.getClass().getResource("/styles/padding.css").toExternalForm();
        scene.getStylesheets().add(font);
        //stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
