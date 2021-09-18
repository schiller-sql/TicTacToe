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

    public Mark getActorToHistoryRecord(int historyPosition) {

        Grid firstGrid = getHistoryRecord(historyPosition);
        Grid secondGrid = getHistoryRecord(historyPosition-1);
        boolean difference = false;
        Mark mark = null;

        while (!difference) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (firstGrid.getMark(i, j) != secondGrid.getMark(i, j)) {
                        difference = true;
                        mark = firstGrid.getMark(i, j);
                    }
                }
            }
        }
        return mark;
    }
}