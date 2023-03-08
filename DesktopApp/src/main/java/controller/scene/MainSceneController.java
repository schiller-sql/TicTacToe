package controller.scene;

import application.Main;
import controller.GameController;
import controller.GameState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import listener.ResizeHelper;
import opponent.Opponent;
import opponent.default_opponents.RandomOpponent;
import persistence.GameRecord;
import persistence.GameRecordStorageException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainSceneController {
    private final GameController controller;
    private Opponent opponent;
    private final HashMap<String, Opponent> opponentClasses = new HashMap<>();
    private double lastX = 0.0d, lastY = 0.0d, lastWidth = 0.0d, lastHeight = 0.0d;

    @FXML
    private HBox boxHead, boxScoreGames, boxScoreWins, boxScoreLosses, boxScoreKD;

    @FXML
    private ToggleGroup opponents;

    @FXML
    private RadioMenuItem RandomOpponent, QuandaryOpponent, MinimaxOpponent, NoobOpponent, TonyRandomOpponent;

    @FXML
    private MenuItem itemAbout;

    @FXML
    private ListView<GameRecord> listGames;

    @FXML
    private VBox boxScores, boxGames;

    @FXML
    private Label lblTotalWins, lblTotalGames, lblTotalLosses, lblKD;

    @FXML
    private Button play;

    public MainSceneController() {
        final Opponent[] availableOpponents = Opponent.defaultOpponents();
        for (Opponent availableOpponent : availableOpponents) {
            opponentClasses.put(availableOpponent.getClass().getSimpleName(), availableOpponent);
        }
        opponent = new RandomOpponent();
        controller = new GameController(opponent);
    }

    @FXML
    public void initialize() {
        RandomOpponent.setToggleGroup(opponents);
        RandomOpponent.setSelected(true);
        MenuItem showHistory, playGame, deleteGame;
        ContextMenu contextMenu;
        updateList();
        updateScores();

        // Create MenuItems and place them in a ContextMenu
        showHistory = new MenuItem("show history");
        playGame = new MenuItem("play");
        deleteGame = new MenuItem("delete");
        contextMenu = new ContextMenu(showHistory, playGame, deleteGame);
        // sets a cell factory on the ListView telling it to use the previously-created ContextMenu (uses default cell factory)
        listGames.setCellFactory(ContextMenuListCell.forListView(contextMenu, (listView) -> new GameRecordListCell()));

        //set the actions to the MenuItems
        showHistory.setOnAction(e -> showHistory(listGames.getSelectionModel().getSelectedItem()));
        playGame.setOnAction(e -> {
            try {
                playGame(listGames.getSelectionModel().getSelectedItem());
                listGames.getSelectionModel().clearSelection();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        deleteGame.setOnAction(e -> {
            try {
                Main.persistentGameRecordStorage.deleteGameRecord(listGames.getSelectionModel().getSelectedItem());
                //listGames.getSelectionModel().clearSelection();
                updateList();
                updateScores();
            } catch (GameRecordStorageException ex) {
                ex.printStackTrace();
            }
        });

        listGames.setOnMouseClicked(event -> {
            MouseButton button = event.getButton();
            if (button == MouseButton.SECONDARY) {
                if (listGames.getSelectionModel().getSelectedItem().getCurrentState() == GameState.running) {
                    playGame.setDisable(false);
                } else if (listGames.getSelectionModel().getSelectedItem().getCurrentState() != GameState.running) {
                    playGame.setDisable(true);
                } else {
                    throw new IllegalArgumentException();
                }
            }
        });
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
        gameSceneController.setController(controller);

        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        ResizeHelper.addResizeListener(stage);
        stage.show();
    }

    public void playGame(GameRecord gameRecord) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/content/game-scene.fxml"));
        Parent root = loader.load();

        GameSceneController gameSceneController = loader.getController();
        gameSceneController.setGameRecord(gameRecord);
        gameSceneController.displayGame();

        Stage stage = (Stage) Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        Scene scene = new Scene(root);
        assert stage != null;
        stage.setScene(scene);
        ResizeHelper.addResizeListener(stage);
        stage.show();
    }

    public void showInfo(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/content/popup/popup.fxml"));
        Stage primaryStage = (Stage) itemAbout.getParentPopup().getOwnerWindow();
        SceneController.submitPopup(loader, primaryStage, "This is a Sample Text");
    }

    private void showHistory(GameRecord gameRecord) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/content/popup/popup.fxml"));
        Stage primaryStage = (Stage) Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        try {
            SceneController.submitPopup(loader, primaryStage, gameRecord.getHistory().toString());
        } catch (GameRecordStorageException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void minimize(ActionEvent actionEvent) {

        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();

        stage.setIconified(true);
    }

    @FXML
    public void maximize(ActionEvent actionEvent) {

        Node n = (Node)actionEvent.getSource();

        Window w = n.getScene().getWindow();

        double currentX = w.getX(), currentY = w.getY(), currentWidth = w.getWidth(), currentHeight = w.getHeight();

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        if( currentX != bounds.getMinX() &&
                currentY != bounds.getMinY() &&
                currentWidth != bounds.getWidth() &&
                currentHeight != bounds.getHeight() ) {

            w.setX(bounds.getMinX());
            w.setY(bounds.getMinY());
            w.setWidth(bounds.getWidth());
            w.setHeight(bounds.getHeight());

            lastX = currentX;  // save old dimensions
            lastY = currentY;
            lastWidth = currentWidth;
            lastHeight = currentHeight;

        } else {

            // de-maximize the window (not same as minimize)

            w.setX(lastX);
            w.setY(lastY);
            w.setWidth(lastWidth);
            w.setHeight(lastHeight);
        }

        actionEvent.consume();  // don't bubble up to title bar
    }

    @FXML
    public void close(ActionEvent actionEvent) {
        ((Button)actionEvent.getSource()).getScene().getWindow().hide();
    }

    public static class ContextMenuListCell<T> extends ListCell<T> {
        public static <T> Callback<ListView<T>, ListCell<T>> forListView(final ContextMenu contextMenu, final Callback<ListView<T>, ListCell<T>> cellFactory) {
            return listView -> {
                ListCell<T> cell = cellFactory.call(listView);
                cell.setContextMenu(contextMenu);
                return cell;
            };
        }
        public ContextMenuListCell(ContextMenu contextMenu) {
            setContextMenu(contextMenu);
        }
    }

    private static class GameRecordListCell extends ListCell<GameRecord> {
        @Override
        protected void updateItem(GameRecord gameRecord, boolean isEmpty) {
            super.updateItem(gameRecord, isEmpty);
            if(gameRecord!=null) {
                if (!isEmpty) {
                    setText(
                            new SimpleDateFormat("MM.dd-HH:mm").format(gameRecord.getLastUpdate())
                                    + " State:" + gameRecord.getCurrentState()
                                    + " Opponent:" + gameRecord.getOpponent().getName()
                                    + "\r\n" + gameRecord.getCurrentGrid().asString("")
                    );
                }
            }
        }
    }

    public void updateList() {
        System.out.println("Update List");
        listGames.getItems().clear();
        for (GameRecord record : Main.persistentGameRecordStorage.getCachedGameRecords()) {
            listGames.getItems().add(record);
        }
    }

    public void updateScores() {
        final var storage = Main.persistentGameRecordStorage;
        int games = storage.total(), wins = storage.wins(), loses = storage.loses();
        double KD, winChance, losesChance;
        if (loses > 0) {
            KD = ((double) wins / (double) loses);
            KD = round(KD, 2);
        } else {
            KD = wins;
        }
        if (wins > 0) {
            winChance = ((double) wins / (double) games);
            winChance = round(winChance, 2);
        } else {
            winChance = 0;
        }
        if (loses > 0) {
            losesChance = ((double) loses / (double) games);
            losesChance = round(losesChance, 2);
        } else {
            losesChance = 0;
        }
        lblTotalGames.setText(String.valueOf(games));
        lblTotalWins.setText(wins + " (" + winChance + "%)");
        lblTotalLosses.setText(loses + " (" + losesChance + "%)");
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
