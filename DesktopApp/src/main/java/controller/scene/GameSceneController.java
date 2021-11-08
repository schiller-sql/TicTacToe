package controller.scene;

import application.Main;
import controller.GameController;
import controller.GameState;
import domain.Mark;
import domain.Point;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import opponent.Opponent;
import opponent.default_opponents.RandomOpponent;
import persistence.GameRecord;
import persistence.GameRecordStorageException;

import java.io.IOException;
import java.util.Objects;

public class GameSceneController {

    private GameController controller;
    private Opponent opponent;
    private Image crossImage, circleImage;
    private boolean isUploaded = false;
    private double lastX = 0.0d, lastY = 0.0d, lastWidth = 0.0d, lastHeight = 0.0d;
    private GameRecord gameRecord;

    @FXML
    Button restart, surrender;
    @FXML
    Button  field00, field10, field20, field01, field11, field21, field02, field12, field22;
    private boolean isExisting;

    public GameSceneController() {
        controller = new GameController(new RandomOpponent());
        isExisting = false;
        gameRecord = null;
    }

    @FXML
    public void initialize() {
        crossImage = new Image(Objects.requireNonNull(getClass().getResource("/images/cross.png")).toExternalForm());
        circleImage = new Image(Objects.requireNonNull(getClass().getResource("/images/circle.png")).toExternalForm());
        restart.setDisable(true);
        restart.setStyle("-fx-text-fill:gray");
        surrender.setDisable(true);
        surrender.setStyle("-fx-text-fill:gray");
        for (Button button : allButtons()) {
            button.setDisable(false);
            button.setGraphic(null);
        }
    }

    public void setController(GameController controller) {
        this.controller = controller;
        opponent = controller.getOpponent();
    }

    public void backToMenu(ActionEvent e) throws IOException {
        if(!isUploaded) {
            uploadGame();
        }
        SceneController.switchToMainScene();
    }

    public void restartGame() {
        restart.setDisable(true);
        restart.setStyle("-fx-text-fill:gray");

        surrender.setDisable(true);
        surrender.setStyle("-fx-text-fill:gray");

        for (Button button : allButtons()) {
            button.setDisable(false);
            button.setGraphic(null);
        }
        isUploaded = uploadGame();
        controller = new GameController(opponent); //TODO opponent
        isUploaded = false;
    }

    public void surrenderGame() { //TODO: something weird happens
        if(controller.getState()!=GameState.running) {
            return;
        }

        restart.setDisable(false);
        restart.setStyle("-fx-text-fill:black");

        surrender.setDisable(true);
        surrender.setStyle("-fx-text-fill:gray");
        for (Button button : allButtons()) {
            button.setDisable(true);
        }
        isUploaded = uploadGame();
    }

    public void selectButton(ActionEvent e) {
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
            isUploaded=uploadGame();
            for (Button button : allButtons()) {
                button.setDisable(true);
                button.setStyle("-fx-opacity: 1;");
            }
            restart.setDisable(false);
            restart.setStyle("-fx-text-fill:black");
            surrender.setDisable(true);
            surrender.setStyle("-fx-text-fill:gray");
        } else {
            restart.setDisable(false);
            restart.setStyle("-fx-text-fill:black");
            surrender.setDisable(false);
            surrender.setStyle("-fx-text-fill:black");
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

    public boolean uploadGame() {
        System.out.print("Upload Game: ");
        if(isExisting) {
            System.out.println("Existing record in List");
            try {
                gameRecord.updateWithController(controller);
            } catch (GameRecordStorageException ex) {
                ex.printStackTrace();
                return false;
            }
        } else {
            System.out.println("New record in List");
            try {
                Main.persistentGameRecordStorage.addGameRecord(controller);
            } catch (GameRecordStorageException e) {
                e.printStackTrace();
                return false;
            }
        }
        System.out.println();
        return true;
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

    public void displayGame() {
        Button btn;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                btn = getButtonForPoint(new Point(x,y));
                if(controller.getGrid().getMark(x,y) == Mark.self) {
                    btn.setGraphic(imageViewFromImage(crossImage));
                    btn.setDisable(true);
                    btn.setStyle("-fx-opacity: 1;");
                } else if(controller.getGrid().getMark(x,y) == Mark.opponent) {
                    btn.setGraphic(imageViewFromImage(circleImage));
                    btn.setDisable(true);
                    btn.setStyle("-fx-opacity: 1;");
                }
            }
        }
    }

    public void setGameRecord(GameRecord gameRecord) {
        this.gameRecord = gameRecord;
        try {
            setController(gameRecord.getController());
        } catch (GameRecordStorageException e) {
            e.printStackTrace();
        }
    }

    public void setExisting(boolean isExisting) {
        this.isExisting = isExisting;
    }
}
