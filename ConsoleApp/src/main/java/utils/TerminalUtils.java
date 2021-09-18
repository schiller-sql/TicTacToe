package utils;

import exceptions.TicTacToeMenuException;
import exceptions.TicTacToeQuitException;
import exceptions.TicTacToeRestartException;
import exceptions.TicTacToeRestartSameOpponentException;

import java.util.Scanner;

public class TerminalUtils {

    public static String colorString(String s, String color) {
        return color + s + TerminalColors.reset;
    }

    public static void printColor(String message, String color) {
        System.out.println(colorString(message, color));
    }

    public static void printStatus(String message) {
        printColor(message, TerminalColors.cyan);
    }

    public static void printError(String message) {
        printColor(message, TerminalColors.yellow);
    }

    private static void inputMarker() {
        System.out.print(colorString("> ", TerminalColors.green));
    }


    public static String getInput(Scanner scanner) {
        inputMarker();
        return scanner.next();
    }

    private static String getInputWithAllExits(Scanner scanner) throws TicTacToeQuitException, TicTacToeRestartException, TicTacToeRestartSameOpponentException, TicTacToeMenuException {
        final String input = getInputWithExits(scanner);
        if (input.matches(":r(estart)?")) {
            throw new TicTacToeRestartException();
        } else if (input.matches(":m(enu)?")) {
            throw new TicTacToeMenuException();
        } else if (input.matches(":s(urrender)?")) {
            throw new TicTacToeRestartSameOpponentException();
        }
        return input;
    }

    public static String getInputWithExits(Scanner scanner) throws TicTacToeQuitException {
        inputMarker();
        final String input = scanner.next();
        if (input.matches(":q(uit)?")) {
            throw new TicTacToeQuitException();
        }
        return input;
    }

    public static int getIntInputWithAllExits(int start, int end) throws
            TicTacToeQuitException, TicTacToeRestartException, TicTacToeRestartSameOpponentException, TicTacToeMenuException {
        final Scanner scanner = new Scanner(System.in);
        while (true) {
            final String rawInput = getInputWithAllExits(scanner);
            try {
                final int input = Integer.parseInt(rawInput);
                if (input < start) {
                    TerminalUtils.printError(input + " is too small, the minimum is " + start + ", choose a new number:");
                } else if (input > end) {
                    TerminalUtils.printError(input + " is too big, the maximum is " + end + ", choose a new number:");
                } else {
                    return input;
                }
            } catch (NumberFormatException ignored) {
                TerminalUtils.printError("\"" + rawInput + "\"" + " is not a number");
            }
        }
    }
}
