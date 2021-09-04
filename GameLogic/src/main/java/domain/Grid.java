package domain;


/**
 * Represents a 3 by 3 tic-tac-toe grid
 */
public interface Grid {
    Mark[][] getGrid();

    boolean markIsEmpty(Point point);

    boolean markIs(Point point, Mark markType);
}
