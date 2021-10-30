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
import javafx.stage.Window;
import javafx.util.Callback;
import opponent.Opponent;
import opponent.default_opponents.RandomOpponent;
import persistence.GameRecord;
import persistence.GameRecordStorageException;
import persistence.SQLitePersistentGameRecordStorage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainSceneController {

    private final GameController controller;
    private Opponent opponent;
    private final HashMap<String, Opponent> opponentClasses = new HashMap();
    private SQLitePersistentGameRecordStorage storage;
    private MenuItem showHistory, playGame, deleteGame;
    ContextMenu contextMenu;

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
        for (Opponent availableOpponent : availableOpponents) {
            opponentClasses.put(availableOpponent.getClass().getSimpleName(), availableOpponent);
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

        // Create MenuItems and place them in a ContextMenu
        showHistory = new MenuItem("show history");
        playGame = new MenuItem("play");
        deleteGame = new MenuItem("delete");
        contextMenu = new ContextMenu(showHistory, playGame, deleteGame);
        // sets a cell factory on the ListView telling it to use the previously-created ContextMenu (uses default cell factory)
        listGames.setCellFactory(ContextMenuListCell.forListView(contextMenu));
        //set the actions to the MenuItems
        showHistory.setOnAction(e -> {
            showHistory((GameRecord) listGames.getSelectionModel().getSelectedItem());
        });
        playGame.setOnAction(e -> {
            try {
                playGame((GameRecord) listGames.getSelectionModel().getSelectedItem());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        deleteGame.setOnAction(e -> {
            try {
                storage.deleteGameRecord((GameRecord) listGames.getSelectionModel().getSelectedItem());
            } catch (GameRecordStorageException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void showHistory(GameRecord gameRecord) {
        //shows the game history
    }

    public List<String> gameRecordsToString() {
        List<String> records = new ArrayList<>();
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

    public void playGame(GameRecord gameRecord) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/content/game-scene.fxml"));
        Parent root = loader.load();

        GameSceneController gameSceneController = loader.getController();
        gameSceneController.setStorage(storage);
        gameSceneController.setController(gameRecord.getController());

        Stage stage = (Stage) Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /*public void updateList() {
        listGames.getItems().clear();
        for(String s : gameRecordsToString()) {
            listGames.getItems().add(s);
        }
    }*/

    public void updateList() {
        listGames.getItems().clear();
        for(GameRecord record : storage.getCachedGameRecords()) {
            listGames.getItems().add(record);
        }
    }

    public void updateScores() {
        int games = storage.total(), wins = storage.wins(), loses = storage.loses();
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

    public static class ContextMenuListCell<T> extends ListCell<T> {

        public static <T> Callback<ListView<T>,ListCell<T>> forListView(ContextMenu contextMenu) {
            return forListView(contextMenu, null);
        }

        public static <T> Callback<ListView<T>,ListCell<T>> forListView(final ContextMenu contextMenu, final Callback<ListView<T>, ListCell<T>> cellFactory) {
            return listView -> {
                ListCell<T> cell = cellFactory == null ? new DefaultListCell<>() : cellFactory.call(listView);
                cell.setContextMenu(contextMenu);
                return cell;
            };
        }

        public ContextMenuListCell(ContextMenu contextMenu) {
            setContextMenu(contextMenu);
        }

    }

    public static class DefaultListCell<T> extends ListCell<T> {
        @Override public void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else if (item instanceof Node newNode) {
                setText(null);
                Node currentNode = getGraphic();
                if (currentNode == null || ! currentNode.equals(newNode)) {
                    setGraphic(newNode);
                }
            } else {
                setText(item == null ? "null" : item.toString());
                setGraphic(null);
            }
        }

    }
}
