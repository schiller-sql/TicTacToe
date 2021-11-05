package controller.scene;

import controller.popup.PopupController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import persistence.GameRecordStorageException;

import java.io.IOException;

public class SceneController {

    public static void switchToMainScene() {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/content/main-scene.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainSceneController mainSceneController = loader.getController();
        mainSceneController.updateList();
        mainSceneController.updateScores();
        Stage stage = (Stage) Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void submitPopup(FXMLLoader loader, Stage primaryStage, String content) {
        Stage inputStage;
        Scene newScene = null;
        try {
            newScene = new Scene(loader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        PopupController popupController = loader.getController();
        popupController.addContent(content);
        inputStage = new Stage();
        inputStage.initStyle(StageStyle.UNDECORATED);
        inputStage.initOwner(primaryStage);
        inputStage.setScene(newScene);
        inputStage.showAndWait();
    }
}
