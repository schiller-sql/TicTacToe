package controller.popup;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.util.List;

public class PopupController{

    @FXML
    private ScrollPane scrollContent;

    @FXML
    private Button btnClose;

    public void closePopup(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void addContent(String content) {
        Label lbl = new Label();
        lbl.setText(content);
        lbl.minWidthProperty().bind(Bindings.createDoubleBinding(() ->
                scrollContent.getViewportBounds().getWidth(),
                scrollContent.viewportBoundsProperty()));
        scrollContent.prefHeightProperty().bind(scrollContent.heightProperty());
        lbl.setAlignment(Pos.CENTER);
        scrollContent.setContent(lbl);
    }

    public void clearContent() {

    }

    public void deleteContent(String content) {

    }

    public void deleteContent(List<String> content) {

    }

    public void deleteContent(int depth) {

    }
}
