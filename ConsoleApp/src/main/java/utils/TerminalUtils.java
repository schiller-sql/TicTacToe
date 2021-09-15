package utils;

import java.util.Scanner;

public class TerminalUtils {

    public static String colorString(String s, String color) {
        return color + s + TerminalColors.reset;
    }

    //TODO: Tony kontrollieren!!!!!
    public static int getIntInput(int start, int end) {
        final Scanner scanner = new Scanner(System.in);
        while (true) {
            final String rawInput = scanner.next();
            try {
                final int input = Integer.parseInt(rawInput);
                if(input < start) {
                    System.err.println(input + " is too small, the minimum is " + start + ", choose a new number:");
                } else if(input > end) {
                    System.err.println(input + " is too big, the maximum is " + end + ", choose a new number:");
                } else {
                    return input;
                }
            } catch (NumberFormatException ignored) {
                System.err.println("\"" + rawInput + "\"" + " is not a number");
            }
        }
    }
}
