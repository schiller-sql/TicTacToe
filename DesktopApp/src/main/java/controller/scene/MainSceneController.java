package controller.scene;

import controller.GameController;
import controller.GameState;
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
import persistence.GameRecord;
import persistence.SQLitePersistentGameRecordStorage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainSceneController {

    private GameController controller;
    private Opponent opponent;
    private HashMap<String, Opponent> opponentClasses = new HashMap();
    SQLitePersistentGameRecordStorage storage;

    @FXML
    RadioMenuItem RandomOpponent = new RadioMenuItem(); //Default Opponent

    @FXML
    ToggleGroup opponents = new ToggleGroup();

    @FXML
    Button play;

    @FXML
    ListView listGames = new ListView();

    @FXML
    Label lblTotalWins, lblTotalGames, lblTotalLosses, lblKD;

    public MainSceneController() {
        final Opponent[] availableOpponents = Opponent.defaultOpponents();
        for (int i = 0; i < availableOpponents.length; i++) {
            opponentClasses.put(availableOpponents[i].getClass().getSimpleName(), availableOpponents[i]);
        }
        opponent = new RandomOpponent();
        controller = new GameController(opponent);

        try {
            storage = new SQLitePersistentGameRecordStorage("userdata");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        RandomOpponent.setToggleGroup(opponents);
        RandomOpponent.setSelected(true);
        updateList();
        updateScores();
    }

    public List<String> gameRecordsToString() {
        List<String> records = new ArrayList<String>();
        for(GameRecord record : storage.getCachedGameRecords()) {
            records.add(
                    new SimpleDateFormat("MM.dd-HH:mm").format(record.getLastUpdate())
                            + " State:" + record.getCurrentState()
                            + " Opponent:" + record.getOpponent().getName()
                            + "\r\n" + record.getCurrentGrid().asString());
        }
        return records;
    }

    public void selectOpponent(ActionEvent e) {
        String Opponent = ((MenuItem) e.getSource()).getText();
        opponent = opponentClasses.get(Opponent);
        controller.setOpponent(opponent);
    }

    public void playGame(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/content/game-scene.fxml"));
        Parent root = loader.load();

        GameSceneController gameSceneController = loader.getController();
        gameSceneController.setStorage(storage);
        gameSceneController.setController(controller);

        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void updateList() {
        listGames.getItems().clear();
        for(String s : gameRecordsToString()) {
            listGames.getItems().add(s);
        }
    }

    public void updateScores() {
        int games = 0, wins = 0, loses = 0;
        games = storage.total();
        wins = storage.wins();
        loses = storage.loses();
        double KD, winChance, losesChance;
        if(loses > 0) {
            KD = ((double) wins / (double) loses);
            KD = round(KD, 2);
        } else {
            KD = wins;
        }
        if(wins > 0) {
            winChance = ((double) wins / (double) games);
            winChance = round(winChance, 2);
        } else {
            winChance = 0;
        }
        if(loses > 0) {
            losesChance = ((double) loses / (double) games);
            losesChance = round(losesChance, 2);
        } else {
            losesChance = 0;
        }
        lblTotalGames.setText(String.valueOf(games));
        lblTotalWins.setText(wins + " (" + winChance + "%)");
        lblTotalLosses.setText(loses + " (" + losesChance +  "%)");
        lblKD.setText(String.valueOf(KD));
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        return (double) Math.round(
                value * (
                        (long) Math.pow(10, places))
        )
                / (
                        (long) Math.pow(10, places)
        );
    }
}
