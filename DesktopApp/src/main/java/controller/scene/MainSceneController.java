package controller.scene;

import controller.GameController;
import domain.Grid;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import opponent.Opponent;
import opponent.default_opponents.RandomOpponent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MainSceneController {

    private GameController controller;
    private Opponent opponent;
    private HashMap<String, Opponent> opponentClasses = new HashMap();

    @FXML
    RadioMenuItem RandomOpponent = new RadioMenuItem(); //Default Opponent

    @FXML
    ToggleGroup opponents = new ToggleGroup();

    @FXML
    Button play;

    @FXML
    ListView listGames;

    @FXML
    Label lblTotalWins, lblTotalGames, lblWinChance;

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
        RandomOpponent.setToggleGroup(opponents);
        RandomOpponent.setSelected(true);
    }


    public void selectOpponent(ActionEvent e) {
        String Opponent = ((MenuItem) e.getSource()).getText();
        opponent = opponentClasses.get(Opponent);
        controller.setOpponent(opponent);
    }

    public void playGame(ActionEvent e) throws IOException {
        //TODO: give attributes to game scene controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/content/game-scene.fxml"));
        Parent root = loader.load();

        //MainSceneController controller = loader.getController(); //to give attributes to the MainSceneController

        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void addGame(List<String> games) {
        for(String s : games) {
            listGames.getItems().add(s);
        }
    }

    public void setStatistics(int wins, int games, int chance) {
        lblTotalWins.setText(String.valueOf(wins));
        lblTotalGames.setText(String.valueOf(games));
        lblWinChance.setText(String.valueOf(chance));

    }

    //TODO: if gamestate is running, then can load the gameScene with these grid
}
