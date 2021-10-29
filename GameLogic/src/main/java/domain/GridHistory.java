package domain;

import java.util.List;
import java.util.Objects;

/**
 * Represents the History of a tic-tac-toe game,
 * by owning each Grid in the game's timeline
 */
public record GridHistory(List<Grid> gridList) {
    /**
     * @param gridList The gridList can contain max. 10 Grids,
     *                 including an empty one from the start,
     *                 it should also contain at least one Grid
     */
    public GridHistory {
        assert (gridList.size() <= 10);
        assert (gridList.size() >= 1);
    }

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
     * @return Last history record, null if non-existent
     */
    public Grid getLastHistoryRecord() {
        if(gridList.isEmpty()) {
            return null;
        }
        return gridList.get(gridList.size() - 1);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GridHistory that = (GridHistory) o;
        return gridList.equals(that.gridList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gridList);
    }
}