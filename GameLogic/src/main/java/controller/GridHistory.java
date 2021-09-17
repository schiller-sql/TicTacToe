package controller;

import domain.Grid;

import java.util.List;

/**
 * Represents the History of a tic-tac-toe game,
 * by owning each Grid in the game's timeline
 */
public record GridHistory(List<Grid> gridList) {
    /**
     * @return How many Grids are contained in this class
     */
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
