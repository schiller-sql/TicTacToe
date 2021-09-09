package domain;


/**
 * Represents a 3 by 3 tic-tac-toe grid
 */
public interface Grid {
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

    /**
     * Get a Mark in the grid by its coordinates
     *
     * Is more performant than
     * using the Point class for the coordinates
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return The Mark which is searched for
     */
    Mark getMark(int x, int y);

    /**
     * Get all positions of a mark type in this Grid
     *
     * @param type The type of Mark to look for,
     *             null means looking for an empty field
     * @return All positions as a Point array
     */
    Point[] getAllOfMarkType(Mark type);
}
