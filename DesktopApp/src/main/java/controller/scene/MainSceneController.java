package controller.scene;

import controller.GameController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import opponent.Opponent;
import opponent.default_opponents.MinimaxOpponent;

import java.io.IOException;

public class MainSceneController {

    @FXML
    Button button;

    /**
     * First select a RiadioMenueItem
     * Then set the current opponent to the radiomenueitem
     * HashMap with radiomenueitems and opponents
     */

    private GameController controller = new GameController(new MinimaxOpponent());
    private Opponent opponent;

    public void selectButton(ActionEvent e) throws IOException {
        ImageView imageView = new ImageView(getClass().getResource("/images/cross.png").toExternalForm());
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        this.button = (Button) e.getSource();
        button.setGraphic(imageView);
    }

    public void selectOpponent(ActionEvent e) {
        opponent = (Opponent) e.getSource();
        controller = new GameController(opponent);
        System.out.println("The opponent \"" + opponent.getClass().getSimpleName() + "\" was chosen\n");
    }
}
