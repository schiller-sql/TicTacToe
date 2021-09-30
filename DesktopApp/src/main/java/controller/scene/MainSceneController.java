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

import java.util.*;

public class MainSceneController {

    private Button button;
    private GameController controller;
    private Opponent opponent;
    private ImageView imageViewCross, imageViewCircle;
    private HashMap<String, Opponent> opponentClasses = new HashMap();
    private HashMap<Button, Point> field = new HashMap(); //TODO: make field key=string of button fxId
    private List<Button> buttons;

    @FXML
    RadioMenuItem RandomOpponent = new RadioMenuItem(); //Default Opponent

    @FXML
    ToggleGroup opponents = new ToggleGroup();

    @FXML
    MenuItem start, restart, surrender;

    @FXML
    Button field00 = new Button(),
            field10 = new Button(),
            field20 = new Button(),
            field01 = new Button(),
            field11 = new Button(),
            field21 = new Button(),
            field02 = new Button(),
            field12 = new Button(),
            field22 = new Button();

    public MainSceneController(){
        final Opponent[] availableOpponents = Opponent.defaultOpponents();
        for (int i = 0; i < availableOpponents.length; i++) {
            opponentClasses.put(availableOpponents[i].getClass().getSimpleName(), availableOpponents[i]);
        }

        opponent = opponentClasses.get(RandomOpponent.toString());
        controller = new GameController(opponent);

        buttons = Arrays.asList(field00, field10, field20, field01, field11, field21, field02, field12, field22);
        int count = 0;
        for(int y = 0; y < 3; y++) {
            for(int x = 0; x < 3; x++) {
                field.put(buttons.get(count), new Point(x, y)); //TODO: buttons.get(count) is null?
                        count++;
            }
        }
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

    public void selectButton(ActionEvent e){ //TODO abfrage ob button schon image hat
        this.button = (Button) e.getSource();
        Point playerPoint = field.get(button); //TODO: field.get(button) is null
        Point opponentPoint = controller.setPoint(playerPoint); //called with null
        button.setGraphic(imageViewCross);
        Button opponentField = (Button) field.values().stream().filter(p -> p.equals(opponentPoint));
        opponentField.setGraphic(imageViewCircle);
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
