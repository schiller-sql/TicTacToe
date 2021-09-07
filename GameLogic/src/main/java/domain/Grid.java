package domain;


/**
 * Represents a 3 by 3 tic-tac-toe grid
 */
public interface Grid {
    /**
     * Get the Grid as a whole
     *
     * @apiNote Do not mutate the given array
     * @return The Grid as a two-dimensional Mark array
     */
    Mark[][] getGrid();

    /**
     * Find out if a field is empty
     *
     * @param point The Point of the field in question
     * @return True if the point is empty
     */
    boolean markIsEmpty(Point point);

    /**
     * Get the Mark of a field
     *
     * @param point The Point of the field in question
     * @return The Mark which is searched for
     */
    Mark getMark(Point point);
}
