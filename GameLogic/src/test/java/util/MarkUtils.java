package util;

import domain.Mark;

import java.util.HashMap;

public class MarkUtils {
    private static final HashMap<Character, Mark> markCharMap = new HashMap<>(3);
    private static final HashMap<Integer, Mark> markIntMap = new HashMap<>(3);

    static {
        markCharMap.put('∙', null);
        markCharMap.put('X', Mark.self);
        markCharMap.put('O', Mark.opponent);

        markIntMap.put(0, null);
        markIntMap.put(1, Mark.self);
        markIntMap.put(2, Mark.opponent);
    }


    public static Mark markFromInt(int i) {
        return markIntMap.get(i);
    }

    public static Mark[][] getGridDataFromString(String rawGrid) {
        assert (rawGrid.length() == 30); //assert
        rawGrid = rawGrid.replaceAll("\n", " | ").replaceAll("\r", " | "); //format
        final Mark[][] grid = new Mark[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grid[i][j] = markCharMap.get(rawGrid.charAt(0));
                rawGrid = rawGrid.substring(3).trim();
            }
        }
        return grid;
    }
}
