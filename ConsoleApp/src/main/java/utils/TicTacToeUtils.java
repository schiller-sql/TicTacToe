package utils;

import controller.GameState;
import domain.Grid;
import domain.Mark;

import java.util.HashMap;

public class TicTacToeUtils {
    static private final HashMap<Mark, String> markStringMap = new HashMap<>();
    static public final HashMap<GameState, String> gameStateStringMap = new HashMap<>();

    static {
        markStringMap.put(Mark.self, TerminalUtils.colorString("X", TerminalColors.red));
        markStringMap.put(Mark.opponent, TerminalColors.blue + "O" + TerminalColors.reset);

        gameStateStringMap.put(GameState.won, TerminalUtils.colorString("You", TerminalColors.red) + " have won!");
        gameStateStringMap.put(GameState.lost, "The " + TerminalUtils.colorString("opponent", TerminalColors.blue) + " has won!");
        gameStateStringMap.put(GameState.tie, "Nobody won, there was a tie!");
    }

    public static String gameStateToString(GameState gameState) {
        return gameStateStringMap.get(gameState);
    }

    public static String markToString(Mark mark) {
        return markStringMap.get(mark);
    }

    private static String markToStringWithAlternative(Mark mark, String alternative) {
        if (mark == null) {
            return TerminalColors.black + alternative + TerminalColors.reset;
        }
        return markToString(mark);
    }

    public static String gridToString(Grid grid) {
        StringBuilder stringBuffer = new StringBuilder();
        int count = 1;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                final Mark mark = grid.getMark(x, y);
                final String markAsString = " " + markToStringWithAlternative(mark, Integer.toString(count)) + " ";
                stringBuffer
                        .append(markAsString)
                        .append("|");
                count++;
            }
            stringBuffer
                    .deleteCharAt(stringBuffer.length() - 1)
                    .append("\n");
            if (y < 2) {
                stringBuffer
                        .append("---+---+---")
                        .append("\n");
            }
        }
        return stringBuffer.toString();
    }
}
