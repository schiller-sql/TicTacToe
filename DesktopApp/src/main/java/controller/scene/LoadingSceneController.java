package controller.scene;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;

public class LoadingSceneController {

    @FXML
    Button button;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void selectButton(ActionEvent e) throws IOException{
        Circle circle = new Circle(15);
        this.button= (Button) e.getSource();
        button.setShape(circle);
    }

    public void switchToMainScene(ActionEvent e) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/content/main-scene.fxml"));
        root = loader.load();

        MainSceneController controller = loader.getController();

        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
