package util;

import domain.Mark;

import java.util.HashMap;

public class GridUtil {
    private static HashMap<Character, Mark> markState = new HashMap<Character, Mark>(3);

    static {
        markState.put('X', Mark.self);
        markState.put('O', Mark.opponent);
        markState.put('∙', null);
    }

    public static Mark[][] getGrid(String stringGrid) {
        assert (stringGrid.length() == 30); //assert
        stringGrid = stringGrid.replaceAll("\n", " | ").replaceAll("\r", " | "); //format
        final Mark[][] grid = new Mark[3][3];

        for (int i = 0; i < 3; i++) { //TODO streams
            for (int j = 0; j < 3; j++) {
                grid[i][j] = markState.get(stringGrid.charAt(0));
                stringGrid = stringGrid.substring(3).trim();
            }
        }
        return grid;
    }
}
