package controller.scene;

import controller.GameController;
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
    Label lblTotalWins, lblTotalGames, lblWinChance, lblTotalLosses, lblKD;
    private MainSceneController mainSceneController;

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

        GameSceneController gameSceneController = loader.getController();
        gameSceneController.setStatistics(
                getLblData(lblTotalWins.getText(), false).intValue(),
                getLblData(lblTotalGames.getText(), false).intValue(),
                getLblData(lblWinChance.getText(), true).doubleValue(),
                listGames.getItems()
        );
        gameSceneController.setController(controller);

        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private Number getLblData(String s, boolean isDouble) {
        if(!isDouble) {
            if (s.matches("[0-9]")) {
                return Integer.parseInt(s);
            }
            return 0;
        } else {
            if (s.matches("[0-9]")) {
                return Double.parseDouble(s);
            }
            return 0;
        }
    }

    public void addGame(List<String> games) {
        for(String s : games) {
            listGames.getItems().add(s);
        }
        //TODO: make listGames add playable option for running games
    }

    public void setStatistics(int wins, int games, int losses, double chance, double KD) {
        lblTotalWins.setText(String.valueOf(wins)); //plus percent tag
        lblTotalGames.setText(String.valueOf(games)); //minus running games
        lblWinChance.setText(chance + "%");
        lblTotalLosses.setText(String.valueOf(losses)); //plus percent tag
        lblKD.setText(KD + "%");

    }

    public void setController(MainSceneController mainSceneController) {
        this.mainSceneController = mainSceneController;
    }

    //TODO: if gamestate is running, then can load the gameScene with these grid
}
