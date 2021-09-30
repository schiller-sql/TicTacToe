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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import opponent.Opponent;
import opponent.default_opponents.RandomOpponent;

import java.util.*;

public class MainSceneController {

    private GameController controller;
    private Opponent opponent;
    private Image crossImage, circleImage;
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
        crossImage = new Image(getClass().getResource("/images/cross.png").toExternalForm());

        circleImage = new Image(getClass().getResource("/images/circle.png").toExternalForm());

        RandomOpponent.setToggleGroup(opponents);
        RandomOpponent.setSelected(true);

        start.setStyle("-fx-text-fill:gray");
        start.setDisable(true);
    }

    private ImageView imageViewFromImage(Image image) {
        final ImageView imageView = new ImageView(image);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        return imageView;
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

        playerButton.setGraphic(imageViewFromImage(crossImage));
        playerButton.setDisable(true);
        playerButton.setStyle("-fx-opacity: 1;");

        final Point opponentPoint = controller.setPoint(playerPoint);
        if(opponentPoint!=null) {
            final Button opponentButton = getButtonForPoint(opponentPoint); //sometimes null
            opponentButton.setGraphic(imageViewFromImage(circleImage));
            opponentButton.setDisable(true);
            opponentButton.setStyle("-fx-opacity: 1;");
        }
        if (controller.getState() != GameState.running) {
            for (Button button : allButtons()) {
                button.setDisable(true);
                button.setStyle("-fx-opacity: 1;");
            }
        }
    }

    public void selectOpponent(ActionEvent e) { //TODO should available in a running game?
        String pOpponent = ((MenuItem) e.getSource()).getText();
        opponent = opponentClasses.get(pOpponent);
        controller.setOpponent(opponent);
    }

    public void startGame() {
        if(controller.getState()==GameState.running) {
            return;
        }
        start.setStyle("-fx-text-fill:gray");
        start.setDisable(true);

        restart.setDisable(false);
        restart.setStyle("-fx-text-fill:black");

        surrender.setDisable(false);
        surrender.setStyle("-fx-text-fill:black");

        //TODO: update controller
        for (Button button : allButtons()) {
            button.setDisable(false);
            button.setGraphic(null);
        }
        controller = new GameController(opponent);
    }

    public void restartGame() {
        start.setDisable(false);
        start.setStyle("-fx-text-fill:black");

        restart.setDisable(false);
        restart.setStyle("-fx-text-fill:black");

        surrender.setDisable(false);
        surrender.setStyle("-fx-text-fill:black");

        //TODO: update controller
        for (Button button : allButtons()) {
            button.setDisable(false);
            button.setGraphic(null);
        }
        controller = new GameController(opponent);
    }


    public void surrenderGame() {
        if(controller.getState()!=GameState.running) {
            return;
        }
        start.setDisable(false);
        start.setStyle("-fx-text-fill:black");

        restart.setDisable(true);
        restart.setStyle("-fx-text-fill:gray");

        surrender.setDisable(true);
        surrender.setStyle("-fx-text-fill:gray");
        for (Button button : allButtons()) {
            button.setDisable(true);
        }
    }

}
