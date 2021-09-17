import controller.GridHistory;
import exceptions.TicTacToeMenuException;
import exceptions.TicTacToeQuitException;
import exceptions.TicTacToeRestartException;
import utils.TerminalColors;
import utils.TerminalUtils;
import utils.TicTacToeUtils;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TerminalUtils.printColor("Welcome to TicTacToe", TerminalColors.purple);
        System.out.println();

        TerminalUtils.printColor("You are now in the main menu, here are the commands:", TerminalColors.cyan);
        listCommands();

        try {
            matchCommands();
        } catch (TicTacToeQuitException e) {
            TerminalUtils.printColor("Program successfully exited", TerminalColors.cyan);
        }
    }

    private static GridHistory startGame() throws TicTacToeQuitException {
        try {
            final Game game = new Game();
            TerminalUtils.printColor("You are now back in the main menu", TerminalColors.cyan);
            return game.getHistory();
        } catch (TicTacToeMenuException e) {
            TerminalUtils.printColor("You are now in the main menu", TerminalColors.cyan);
            return null;
        } catch (TicTacToeRestartException e) {
            TerminalUtils.printColor("Game successfully restarted", TerminalColors.cyan);
            return startGame();
        }
    }

    private static void matchCommands() throws TicTacToeQuitException {
        GridHistory lastHistory = null;
        while (true) {
            final Scanner scanner = new Scanner(System.in);
            final String command = TerminalUtils.getInputWithExits(scanner);
            if (command.matches("^:g(game)?$")) {
                lastHistory = startGame();
            } else if (command.matches("^:h(istory)?$")) {
                if (lastHistory != null) {
                    System.out.println(TicTacToeUtils.gridHistoryToString(lastHistory));
                } else {
                    System.out.println("There is no game, to print the history from");
                }
            } else if (command.matches("^:c(commands)?$")) {
                TerminalUtils.printColor("Commands:", TerminalColors.cyan);
                listCommands();
            } else if (command.matches("^:")) {
                TerminalUtils.printError("Not a valid command, for all valid commands see :c(ommands)");
            } else {
                TerminalUtils.printError(
                        "All commands have to be prefixed with ':', " +
                        "for all valid commands see :c(ommands)\""
                );
            }
        }
    }

    private static void listCommands() {
        System.out.println(TerminalUtils.colorString("  :q(uit)", TerminalColors.blue) + ",        quit the program");
        System.out.println(TerminalUtils.colorString("  :m(enu)", TerminalColors.blue) + ",        go back to the menu         (only in a game)");
        System.out.println(TerminalUtils.colorString("  :r(estart)", TerminalColors.blue) + ",     restart the game            (only in a game)");
        System.out.println(TerminalUtils.colorString("  :g(game)", TerminalColors.blue) + ",       start a new game            (only in the main menu)");
        System.out.println(TerminalUtils.colorString("  :h(history)", TerminalColors.blue) + ",    show history of last game   (only in the main menu, after a game)");
        System.out.println(TerminalUtils.colorString("  :c(commands)", TerminalColors.blue) + ",   list all commands           (only in the main menu)");
    }
}
