import controller.GridHistory;
import exceptions.TicTacToeMenuException;
import exceptions.TicTacToeQuitException;
import exceptions.TicTacToeRestartException;
import exceptions.TicTacToeRestartSameOpponentException;
import opponent.Opponent;
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
            matchCommands(directlyGame);
        } catch (TicTacToeQuitException e) {
            TerminalUtils.printStatus("Program successfully exited");
        }
    }

    private static Game startGame(Opponent withOpponent, boolean firstGame) throws TicTacToeQuitException {
        Game game = null;
        try {
            if (withOpponent == null) {
                game = new Game(firstGame);
            } else {
                game = new Game(withOpponent);
            }
            game.start();
            TerminalUtils.printStatus("You are now back in the main menu");
            return game;
        } catch (TicTacToeMenuException e) {
            TerminalUtils.printStatus("You are now in the main menu");
            return null;
        } catch (TicTacToeRestartException e) {
            TerminalUtils.printStatus("Game successfully restarted");
            return startGame(null, false);
        } catch (TicTacToeRestartSameOpponentException e) {
            TerminalUtils.printStatus("Game surrendered, restarting with same opponent");
            return startGame(game != null ? game.getOpponent() : null, false);
        }
    }

    private static void matchCommands(boolean startDirectly) throws TicTacToeQuitException {
        Game lastGame = startDirectly ? startGame(null, true) : null;
        final Scanner scanner = new Scanner(System.in);
        // TODO: Fix the warning
        while (true) {
            final String command = TerminalUtils.getInput(scanner);
            if (command.charAt(0) != ':') {
                TerminalUtils.printError(
                        "All commands have to be prefixed with ':', " +
                        "for all valid commands see :c(ommands)"
                );
                continue;
            }
            switch (command) {
                case "q":
                case "quit":
                    throw new TicTacToeQuitException();
                case "m":
                case "menu":
                    TerminalUtils.printError("You are already in the menu");
                    break;
                case "r":
                case "restart":
                    TerminalUtils.printError("You can only restart, if you are in a game");
                    break;
                case "s":
                case "surrender":
                    TerminalUtils.printError("You can only surrender, if you are in a game");
                    break;
                case "c":
                case "commands":
                    listCommands();
                    break;
                case "g":
                case "game":
                    lastGame = startGame(null, lastGame == null);
                    break;
                case "o":
                case "opponent":
                    if (lastGame != null) {
                        lastGame = startGame(lastGame.getOpponent(), false);
                    } else {
                        TerminalUtils.printError("There is no game, to take the opponent from");
                    }
                    break;
                case "h":
                case "history":
                    if (lastGame != null) {
                        System.out.println(TicTacToeUtils.gridHistoryToString(lastGame.getHistory()));
                    } else {
                        TerminalUtils.printError("There is no game, to print the history from");
                    }
                    break;
                default:
                    TerminalUtils.printError("Not a valid command, for all valid commands see :c(ommands)");
            }
        }
    }

    private static void listCommands() {
        System.out.println(TerminalUtils.colorString("  :q(uit)", TerminalColors.blue) + ",        quit the program");
        System.out.println(TerminalUtils.colorString("  :m(enu)", TerminalColors.blue) + ",        go back to the menu            (only in a game)");
        System.out.println(TerminalUtils.colorString("  :r(estart)", TerminalColors.blue) + ",     restart the game               (only in a game)");
        System.out.println(TerminalUtils.colorString("  :s(urrender)", TerminalColors.blue) + ",   restart but keep the opponent  (only in a game)");
        System.out.println(TerminalUtils.colorString("  :c(commands)", TerminalColors.blue) + ",   list all commands              (only in the main menu)");
        System.out.println(TerminalUtils.colorString("  :g(game)", TerminalColors.blue) + ",       start a new game               (only in the main menu)");
        System.out.println(TerminalUtils.colorString("  :o(pponent)", TerminalColors.blue) + ",    play again with same opponent  (only in the main menu, after a game)");
        System.out.println(TerminalUtils.colorString("  :h(history)", TerminalColors.blue) + ",    show history of last game      (only in the main menu, after a game)");
    }
}
