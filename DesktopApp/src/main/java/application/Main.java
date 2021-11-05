package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import persistence.PersistentGameRecordStorage;
import persistence.SQLitePersistentGameRecordStorage;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {

    public static PersistentGameRecordStorage persistentGameRecordStorage;

    static {
        try {
            persistentGameRecordStorage = new SQLitePersistentGameRecordStorage("userdata.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/content/loading-scene.fxml"));
        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
