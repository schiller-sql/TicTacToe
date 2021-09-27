package controller.scene;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoadingSceneController {

    @FXML
    TextField nameTextField;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void login(ActionEvent e) throws IOException {
        String name = nameTextField.getText();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/content/main-scene.fxml"));
        root = loader.load();

        MainSceneController controller = loader.getController();
        controller.displayName(name);

        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
