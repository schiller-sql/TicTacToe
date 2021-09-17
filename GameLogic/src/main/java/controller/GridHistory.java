package controller;

import domain.Grid;

import java.util.List;

/**
 * Represents the History of a tic-tac-toe game,
 * by owning each Grid in the game's timeline
 */
public final class GridHistory {
    private final List<Grid> gridList;

    /**
     */
    public GridHistory(List<Grid> gridList) {
        this.gridList = gridList;
    }

    /**
     * @return How many Grids are contained in this class
     */
    public int length() {
        return gridList.size();
    }

    /**
     * @return How many Grids are contained in this class
     * @deprecated Replaced getLength() with length()
     */
    @Deprecated
    public int getLength() {
        return gridList.size();
    }

    /**
     * Get a Grid in the timeline
     *
     * @param historyPosition The position of the grid in the timeline,
     *                        the first Grid placed has the history position of 0
     * @return The Grid at the historyPosition
     */
    public Grid getHistoryRecord(int historyPosition) {
        return gridList.get(historyPosition);
    }
}
