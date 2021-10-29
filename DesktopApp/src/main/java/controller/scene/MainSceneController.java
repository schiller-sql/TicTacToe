package controller.scene;

import controller.GameController;
import javafx.collections.ListChangeListener;
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
    Label lblTotalWins, lblTotalGames, lblWinChance, lblTotalLosses, lblKD;

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

    //TODO: if gamestate is running, then can load the gameScene with these grid
}
