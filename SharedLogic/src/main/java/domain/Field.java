package domain;


/**
 * Represents a 3 by 3 tic-tac-toe field
 */
public interface Field {
    public Mark[][] getField();

    public default boolean markIsEmpty(Point point) {
        return getField()[point.x()][point.y()] == null;
    }

    public default boolean markIs(Point point, Mark markType) {
        return getField()[point.x()][point.y()] == markType;
    }
}
