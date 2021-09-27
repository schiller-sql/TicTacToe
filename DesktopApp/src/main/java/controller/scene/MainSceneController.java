package controller.scene;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainSceneController {

    @FXML
    Label nameLabel;

    public void displayName(String name) {
        nameLabel.setText("Hello " + name);
    }

    /**
     * future method...
     *
     * displays the Grid
     */
    public void displayGrid(String gridString) {

    }
}
