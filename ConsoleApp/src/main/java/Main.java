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
        boolean isHelp = false;
        for (String arg : args) {
            if (arg.equals("--help") || arg.equals("-h")) {
                isHelp = true;
                break;
            }
        }

        boolean nonExistingOptionWasFound = false;
        for (String arg : args) {
            if (
                    !arg.equals("--game") &&
                    !arg.equals("-g") &&
                    !arg.equals("--help") &&
                    !arg.equals("-h")
            ) {
                System.err.println("The option '" + arg + "' does not exist, for all options use '--help'");
                nonExistingOptionWasFound = true;
            }
        }
        if (nonExistingOptionWasFound) {
            System.exit(1);
        }

        boolean directlyGame = false;

        for (String arg : args) {
            if (arg.equals("--game") || arg.equals("-g")) {
                if (isHelp) {
                    System.err.println("The option '--help' is a standalone option");
                    System.exit(1);
                } else {
                    directlyGame = true;
                }
            } else {
                System.out.println("tic-tac-toe <options>");
                System.out.println();
                System.out.println("Options:");
                System.out.println("  -h, --help      The help menu");
                System.out.println("  -g, --game      Start directly in a game without");
                System.out.println("                  the commands menu at the beginning");
                System.exit(0);
            }
        }

        handleTicTacToe(directlyGame);
    }

    private static void handleTicTacToe(boolean directlyGame) {
        if (!directlyGame) {
            TerminalUtils.printColor("Welcome to TicTacToe", TerminalColors.purple);
            System.out.println();

            TerminalUtils.printStatus("You are now in the main menu, here are the commands:");
            listCommands();
        }

        try {
            if (directlyGame) {
                startGame(false);
                matchCommands(false);
            } else {
                matchCommands(true);
            }
        } catch (TicTacToeQuitException e) {
            TerminalUtils.printStatus("Program successfully exited");
        }
    }

    private static GridHistory startGame(boolean firstGame) throws TicTacToeQuitException {
        try {
            final Game game = new Game(firstGame);
            TerminalUtils.printStatus("You are now back in the main menu");
            return game.getHistory();
        } catch (TicTacToeMenuException e) {
            TerminalUtils.printStatus("You are now in the main menu");
            return null;
        } catch (TicTacToeRestartException e) {
            TerminalUtils.printStatus("Game successfully restarted");
            return startGame(false);
        }
    }

    private static void matchCommands(boolean firstGame) throws TicTacToeQuitException {
        boolean isFirstGame = firstGame;
        GridHistory lastHistory = null;
        // TODO: Fix the warning
        while (true) {
            // TODO: If there is a Y/n question, n fails, as it can't match
            final Scanner scanner = new Scanner(System.in);
            final String command = TerminalUtils.getInputWithExits(scanner);
            if (command.matches("^:g(game)?$")) {
                lastHistory = startGame(isFirstGame);
                isFirstGame = true;
            } else if (command.matches("^:h(istory)?$")) {
                if (lastHistory != null) {
                    System.out.println(TicTacToeUtils.gridHistoryToString(lastHistory));
                } else {
                    TerminalUtils.printError("There is no game, to print the history from");
                }
            } else if (command.matches("^:c(commands)?$")) {
                TerminalUtils.printStatus("Commands:");
                listCommands();
            } else if (command.matches(":(r(estart)?|m(enu)?)")) {
                TerminalUtils.printError("This command only works inside of a game, for all valid commands see :c(ommands)");
            } else if (command.matches("^:")) {
                TerminalUtils.printError("Not a valid command, for all valid commands see :c(ommands)");
            } else {
                TerminalUtils.printError(
                        "All commands have to be prefixed with ':', " +
                        "for all valid commands see :c(ommands)"
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
