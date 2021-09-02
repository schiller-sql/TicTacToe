package domain;


/**
 * Represents a 3 by 3 tic-tac-toe grid
 */
public interface Grid {
    public Mark[][] getGrid();

    public default boolean markIsEmpty(Point point) {
        return getGrid()[point.x()][point.y()] == null;
    }

    public default boolean markIs(Point point, Mark markType) {
        return getGrid()[point.x()][point.y()] == markType;
    }
}
