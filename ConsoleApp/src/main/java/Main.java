import controller.GameController;
import controller.GameState;
import controller.GridHistory;
import domain.Grid;
import domain.Mark;
import domain.Point;
import opponent.ExampleOpponent;
import opponent.Opponent;
import opponent.RandomOpponent;
import utils.TerminalColors;
import utils.TerminalUtils;
import utils.TicTacToeUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.IntStream;


public class Main {
    static GameController controller;

    public static void main(String[] args) {
        System.out.println("Welcome to TicTacToe");
        System.out.println("\nSelect the number of the opponent you want to play against:");

        boolean status = true;
        final Opponent opponent = getOpponentInput();
        controller = new GameController(opponent);

        System.out.println("The opponent \"" + opponent.getClass().getSimpleName() + "\" was chosen\n");
        printTutorial();

         do {
            game();
        } while(status);
    }

    private static void game() {

        while (controller.getState() == GameState.running) {
            final Point point = getPointInput();
            controller.setPoint(point);
            System.out.println(TicTacToeUtils.gridToString(controller.getGrid()));
        }
        System.out.println(TicTacToeUtils.gameStateToString(controller.getState())+ "\r\n");

        //TODO: Check if the player wants to continue (Else press Q)
        //TODO: add the option to print the game history

        System.out.println("Select a opponent: ");
        final Opponent opponent = getOpponentInput();
        System.out.println("The opponent \"" + opponent.getClass().getSimpleName() + "\" was chosen\n");
        controller = new GameController(opponent);
        System.out.println(TicTacToeUtils.gridToString(controller.getGrid()));
    }

    private static void printTutorial() {
        System.out.println(TicTacToeUtils.gridToString(controller.getGrid()));

        System.out.println("Press the number key of the respective field");
        System.out.println("you want to place your cross on");
        System.out.println();

        System.out.println("You are: " + TicTacToeUtils.markToString(Mark.self));
        System.out.println("And the opponent is: " + TicTacToeUtils.markToString(Mark.opponent));
        System.out.println();

        System.out.print("Please type a number between ");
        System.out.print(TerminalUtils.colorString("1", TerminalColors.black) + " and ");
        System.out.print(TerminalUtils.colorString("9", TerminalColors.black));
        System.out.println(" to select a field:");
    }


    @SuppressWarnings("unused")
    private static void printHistory() {
        final GridHistory history = controller.getHistory();
        for (int i = 0; i < history.getLength(); i++) {
            final Grid grid = history.getHistoryRecord(i);
            System.out.println(TicTacToeUtils.gridToString(grid));
        }
    }


    private static Opponent getOpponentInput() {
        final Opponent[] opponents = new Opponent[]{
                new RandomOpponent(),
                new ExampleOpponent(),
        };

        for (int i = 0; i < opponents.length; i++) {
            System.out.println(i + 1 + ": " + opponents[i].getClass().getSimpleName());
        }
        final int choice = TerminalUtils.getIntInput(1, opponents.length);
        return opponents[choice - 1];
    }

    private static Point getPointInput() {
        int count = 0;
        HashMap<Integer, Point> map = new HashMap<>();
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                count++;
                map.put(count, new Point(x, y));
            }
        }

        int input = -1;
        do {
            if (input == -1) {
                System.out.println("Choose your Point:");
            } else {
                System.err.println("The point was already taken, choose a new one:");
            }
            input = TerminalUtils.getIntInput(1, 9);
        } while (!controller.getGrid().markIsEmpty(map.get(input)));
        return map.get(input);
    }
}
