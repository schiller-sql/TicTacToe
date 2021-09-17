package util;

import domain.Grid;

public class GridUtils {
    public static Grid getGridFromString(String rawGrid) {
        return new Grid(MarkUtils.getGridDataFromString(rawGrid));
    }

    public static boolean gridIsFull(Grid grid) {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (grid.getMark(x, y) == null) {
                    return false;
                }
            }
        }
        return true;
    }

}
