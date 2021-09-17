package utils;

import exceptions.TicTacToeMenuException;
import exceptions.TicTacToeQuitException;
import exceptions.TicTacToeRestartException;

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

    public static boolean getBooleanInput(Scanner scanner, String question) {
        System.out.println(TerminalUtils.colorString(question, TerminalColors.cyan) + " (Y/n)");
        String input = null;
        do {
            if (input != null) {
                TerminalUtils.printError("Invalid input, only Y(es) and n(o) is allowed");
            }
            inputMarker();
            input = scanner.next();
        } while (!input.matches("^(Y(es)?|no?)$"));
        return !input.matches("^no?$");
    }

    public static String getInput(Scanner scanner) {
        inputMarker();
        return scanner.next();
    }

    private static String getInputWithAllExits(Scanner scanner) throws TicTacToeQuitException, TicTacToeMenuException, TicTacToeRestartException {
        final String input = getInputWithExits(scanner);
        if (input.matches("^:r(estart)?$")) {
            final boolean finalSay = getBooleanInput(scanner, "Do you really want to restart?");
            if (finalSay) {
                throw new TicTacToeRestartException();
            } else {
                System.out.println("Restart terminated");
            }
        } else if (input.matches("^:m(enu)?$")) {
            final boolean finalSay = getBooleanInput(scanner, "Do you really want to go to the menu?");
            if (finalSay) {
                throw new TicTacToeMenuException();
            } else {
                System.out.println("Exit terminated");
            }
        }
        return input;
    }

    public static String getInputWithExits(Scanner scanner) throws TicTacToeQuitException {
        inputMarker();
        final String input = scanner.next();
        if (input.matches("^:q(uit)?$")) {
            final boolean finalSay = getBooleanInput(scanner, "Do you really want to quit?");
            if (finalSay) {
                throw new TicTacToeQuitException();
            } else {
                System.out.println("Quitting terminated");
            }
        }
        return input;
    }

    public static int getIntInputWithAllExits(int start, int end) throws
            TicTacToeQuitException, TicTacToeMenuException, TicTacToeRestartException {
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
