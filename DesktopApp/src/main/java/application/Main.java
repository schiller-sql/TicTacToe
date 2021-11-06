package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import listener.ResizeHelper;
import listener.WindowMoveHelper;
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
        Scene scene;
        String osName = System.getProperty("os.name");
        if( osName != null && osName.startsWith("Windows") ) {

            //
            // Windows hack b/c unlike Mac and Linux, UNDECORATED doesn't include a shadow
            //
            scene = (new WindowsHack()).getShadowScene(root);
            stage.initStyle(StageStyle.TRANSPARENT);

        } else {
            scene = new Scene(root);
            stage.initStyle(StageStyle.UNDECORATED);
        }

        stage.setScene(scene);
        stage.show();
        WindowMoveHelper.addMoveListener(stage); //TODO:
    }

    private class WindowsHack {
        public Scene getShadowScene(Parent p) {
            Scene scene;
            AnchorPane outer = new AnchorPane();
            outer.getChildren().add( p );
            outer.setPadding(new Insets(10.0d));
            outer.setBackground( new Background(new BackgroundFill( Color.rgb(0,0,0,0), new CornerRadii(0), new
                    Insets(0))));

            p.setEffect(new DropShadow());
            ((AnchorPane)p).setBackground( new Background(new BackgroundFill( Color.WHITE, new CornerRadii(0), new Insets(0)
            )));
            scene = new Scene( outer );
            scene.setFill( Color.rgb(0,255,0,0) );
            return scene;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
