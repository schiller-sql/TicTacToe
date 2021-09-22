package controller;

import domain.Grid;
import domain.Mark;

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
     * @param historyPosition The position of the gridData in the timeline,
     *                        the first Grid placed has the history position of 0
     * @return The Grid at the historyPosition
     */
    public Grid getHistoryRecord(int historyPosition) {
        return gridList.get(historyPosition);
    }

    /**
     * @param historyPosition The position of the grid in the timeline,
     *                        the first Grid placed has the history position of 0.
     *                        <p>
     *                        Make sure that the historyPosition is at least 1,
     *                        as it has to be compared with the Grid before it
     * @return The Mark type of the last placed mark (in history)
     */
    public Mark getActorToHistoryRecord(int historyPosition) {

        Grid firstGrid = getHistoryRecord(historyPosition);
        Grid secondGrid = getHistoryRecord(historyPosition - 1);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (firstGrid.getMark(i, j) != secondGrid.getMark(i, j)) {
                    return firstGrid.getMark(i, j);
                }
            }
        }
        return null;
    }
}