package controller.scene;

import controller.GameController;
import domain.Point;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import opponent.Opponent;

import java.util.HashMap;

public class MainSceneController {

    private Button button;
    private GameController controller;
    private Opponent opponent;
    private ImageView imageView;
    private HashMap<String, Opponent> opponentClasses = new HashMap();
    private HashMap<Button, Point> field = new HashMap();

    @FXML
    RadioMenuItem RandomOpponent = new RadioMenuItem(); //Default Opponent

    @FXML
    ToggleGroup opponents = new ToggleGroup();

    @FXML
    MenuItem start, restart, surrender;

    public MainSceneController(){
        final Opponent[] availableOpponents = Opponent.defaultOpponents();
        for (int i = 0; i < availableOpponents.length; i++) {
            opponentClasses.put(availableOpponents[i].getClass().getSimpleName(), availableOpponents[i]);
        }

        opponent = opponentClasses.get(RandomOpponent.toString());
        controller = new GameController(opponent);
    }

    @FXML
    public void initialize() {
        imageView = new ImageView(getClass().getResource("/images/cross.png").toExternalForm());
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);

        RandomOpponent.setToggleGroup(opponents);
        RandomOpponent.setSelected(true);
    }

    public void selectButton(ActionEvent e){ //TODO
        this.button = (Button) e.getSource();
        button.setGraphic(imageView);
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
