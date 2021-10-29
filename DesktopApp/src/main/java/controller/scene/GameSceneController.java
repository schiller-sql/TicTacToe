package controller.scene;

import controller.GameController;
import controller.GameState;
import domain.Point;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import opponent.Opponent;
import opponent.default_opponents.RandomOpponent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GameSceneController {
    /*TODO:
    disable restart Button if the Game field is empty
    disable surrender button if the game field is empty
    enable restart after surrender
    make menu button return back to the main menu
    format code
    clean up code
    add confirm popups to restart button and menu button
     */


    private GameController controller;
    private Opponent opponent;
    private Image crossImage, circleImage;
    private List<String> games, newGames;
    int winsTotal, lossesTotal;
    int gamesTotal;
    double chance, KD;

    @FXML
    Button restart, surrender;

    @FXML
    Button  field00,
            field10,
            field20,
            field01,
            field11,
            field21,
            field02,
            field12,
            field22;

    public GameSceneController() {
        controller = new GameController(new RandomOpponent());
        winsTotal = 0;
        gamesTotal = 0;
        chance = 0;
    }

    public void setController(GameController controller) {
        this.controller = controller;
        opponent = controller.getOpponent();
    }

    public void restartGame() {

        restart.setDisable(true);
        restart.setStyle("-fx-text-fill:gray");;

        surrender.setDisable(true);
        surrender.setStyle("-fx-text-fill:gray");

        for (Button button : allButtons()) {
            button.setDisable(false);
            button.setGraphic(null);
        }

        //if(controller.getState() == GameState.running) {
            //games.add(getTime() + " : " + controller.getState().toString() + "\r\n" + controller.getGrid().toString(""));
        //}
        controller = new GameController(opponent); //TODO opponent
    }

    public String getTime() {
        return DateTimeFormatter.ofPattern("MM.dd-HH:mm").format(LocalDateTime.now());
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

        //games.add(getTime() + " : " + controller.getState().toString() + "\r\n" + controller.getGrid().toString(""));
    }

    @FXML
    public void initialize() {
        crossImage = new Image(getClass().getResource("/images/cross.png").toExternalForm());

        circleImage = new Image(getClass().getResource("/images/circle.png").toExternalForm());

        restart.setDisable(true);
        restart.setStyle("-fx-text-fill:gray");

        surrender.setDisable(true);
        surrender.setStyle("-fx-text-fill:gray");

        for (Button button : allButtons()) {
            button.setDisable(false);
            button.setGraphic(null);
        }

        games = new ArrayList<>();
        newGames = new ArrayList<>();
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
            for (Button button : allButtons()) {
                button.setDisable(true);
                button.setStyle("-fx-opacity: 1;");
            }
            restart.setDisable(false);
            restart.setStyle("-fx-text-fill:black");

            surrender.setDisable(true);
            surrender.setStyle("-fx-text-fill:gray");

            //TODO: dave the opponent played against
            System.out.println("Add: " + getTime() + " : " + controller.getState().toString() + "\r\n" + controller.getGrid().toString(""));
            newGames.add(getTime() + " : " + controller.getState().toString() + "\r\n" + controller.getGrid().toString(""));

        } else {
            restart.setDisable(false);
            restart.setStyle("-fx-text-fill:black");

            surrender.setDisable(false);
            surrender.setStyle("-fx-text-fill:black");
        }

    }

    public void backToMenu(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/content/main-scene.fxml"));
        Parent root = loader.load();

        String time = DateTimeFormatter.ofPattern("MM.dd-HH:mm").format(LocalDateTime.now());
        MainSceneController controller = loader.getController(); //to give attributes to the MainSceneController

        controller.addGame(games);
        controller.addGame(newGames);
        for(String s : newGames) {
            s = s.substring(14);
            System.out.println(s.substring(0, s.indexOf("\r\n")));
            switch (s.substring(0, s.indexOf("\r\n"))) {
                case "won":
                    winsTotal++;
                case "lost":
                    lossesTotal++;
            }
            gamesTotal++;
        }

        if(winsTotal>0) {
            chance =  ((double) gamesTotal) / ((double) winsTotal);
        } else {
            chance = 0;
        }
        if(lossesTotal>0) {
            KD =  ((double) winsTotal) / ((double) lossesTotal);
        } else {
            KD = winsTotal;
        }
        System.out.println("winsTotal:" + winsTotal + ", gamesTotal:" + gamesTotal + ", winChance:" + chance);
        controller.setStatistics(winsTotal, gamesTotal, lossesTotal, chance, KD);

        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void setStatistics(int parseInt, int parseInt1, double parseInt2, List<String> games) {
        winsTotal =  parseInt;
        gamesTotal =  parseInt1;
        chance = parseInt2;
        this.games.addAll(games);
    }
}
