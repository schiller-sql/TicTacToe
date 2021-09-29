package controller.scene;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class MainSceneController {

    @FXML
    Button button;

    public void selectButton(ActionEvent e) throws IOException {
        ImageView imageView = new ImageView(getClass().getResource("/images/cross.png").toExternalForm());
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        this.button = (Button) e.getSource();
        button.setGraphic(imageView);
    }


}
