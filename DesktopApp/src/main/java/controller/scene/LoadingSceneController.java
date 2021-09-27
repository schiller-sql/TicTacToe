package controller.scene;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javafx.event.ActionEvent;

public class LoadingSceneController {

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick(ActionEvent actionEvent) {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
