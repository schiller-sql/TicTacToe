package controller.popup;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class PopupController{

    @FXML
    private ScrollPane scrollContent;

    @FXML
    private Button btnClose;

    public void closePopup(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void addContent() {

    }

    public void setContent() {

    }

    public void clearContent() {

    }

    public void deleteContent() {

    }
}
