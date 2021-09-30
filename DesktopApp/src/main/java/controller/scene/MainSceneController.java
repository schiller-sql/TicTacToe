package controller.scene;

import controller.GameController;
import controller.GameState;
import domain.Point;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import opponent.Opponent;
import opponent.default_opponents.RandomOpponent;

import java.util.*;

public class MainSceneController {

    private GameController controller;
    private Opponent opponent;
    private ImageView imageViewCross, imageViewCircle;
    private HashMap<String, Opponent> opponentClasses = new HashMap();

    @FXML
    RadioMenuItem RandomOpponent = new RadioMenuItem(); //Default Opponent

    @FXML
    ToggleGroup opponents = new ToggleGroup();

    @FXML
    MenuItem start, restart, surrender;

    @FXML
    Button field00,
            field10,
            field20,
            field01,
            field11,
            field21,
            field02,
            field12,
            field22;

    public MainSceneController() {
        final Opponent[] availableOpponents = Opponent.defaultOpponents();
        for (int i = 0; i < availableOpponents.length; i++) {
            opponentClasses.put(availableOpponents[i].getClass().getSimpleName(), availableOpponents[i]);
        }

        opponent = new RandomOpponent();
        controller = new GameController(opponent);


    }

    @FXML
    public void initialize() {
        imageViewCross = new ImageView(getClass().getResource("/images/cross.png").toExternalForm());
        imageViewCross.setFitHeight(50);
        imageViewCross.setFitWidth(50);

        imageViewCircle = new ImageView(getClass().getResource("/images/circle.png").toExternalForm());
        imageViewCircle.setFitHeight(50);
        imageViewCircle.setFitWidth(50);

        RandomOpponent.setToggleGroup(opponents);
        RandomOpponent.setSelected(true);
    }

    private Button[] allButtons() {
        return new Button[]{field00, field10, field20, field01, field11, field21, field02, field12, field22};
    }

    private Point getPointForButton(Button button) {
        final Button[] allButtons = allButtons();
        int i = 0;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (allButtons[i] == button) {
                    return new Point(x, y);
                }
                i++;
            }
        }
        throw new Error("Button not inside the grid");
    }

    private Button getButtonForPoint(Point point) {
        final Button[] allButtons = allButtons();
        int i = 0;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (x == point.x() && y == point.y()) {
                    return allButtons[i];
                }
                i++;
            }
        }
        throw new Error("Button not found");
    }

    public void selectButton(ActionEvent e) { //TODO abfrage ob button schon image hat
        final Button playerButton = (Button) e.getSource();
        final Point playerPoint = getPointForButton(playerButton);

        playerButton.setGraphic(imageViewCross);
        playerButton.setDisable(true);

        if (controller.getState() == GameState.running) {
            final Point opponentPoint = controller.setPoint(playerPoint);
            final Button opponentButton = getButtonForPoint(opponentPoint);

            opponentButton.setGraphic(imageViewCircle);
            opponentButton.setDisable(true);
        }
    }

    public void selectOpponent(ActionEvent e) { //TODO should available in a running game?
        String pOpponent = ((MenuItem) e.getSource()).getText();
        opponent = opponentClasses.get(pOpponent);
        controller = new GameController(opponent);
    }

    public void startGame() {
        start.setStyle("-fx-text-fill:gray");
        start.setDisable(true);

        restart.setDisable(false);
        restart.setStyle("-fx-text-fill:black");

        surrender.setDisable(false);
        surrender.setStyle("-fx-text-fill:black");

        //TODO: clear the field
    }

    public void restartGame() {
        start.setDisable(false);
        start.setStyle("-fx-text-fill:black");

        restart.setDisable(false);
        restart.setStyle("-fx-text-fill:black");

        surrender.setDisable(false);
        surrender.setStyle("-fx-text-fill:black");

        //TODO: clear the field
    }


    public void surrenderGame() {
        start.setDisable(false);
        start.setStyle("-fx-text-fill:black");

        restart.setDisable(true);
        restart.setStyle("-fx-text-fill:gray");

        surrender.setDisable(true);
        surrender.setStyle("-fx-text-fill:gray");
    }
}
