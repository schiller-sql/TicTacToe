package controller;

import domain.Grid;
import domain.Mark;
import domain.Point;

/**
 * Mutable version of the Grid class,
 * so the controller can edit the grid
 * <p>
 * Should only be used in the controller,
 * as in the opponent for example,
 * it returns the point to be set
 */
class MutableGrid implements Grid {

    private final Mark[][] grid;


    /**
     * Empty constructor, with a clean grid
     */
    MutableGrid() {
        grid = new Mark[3][3];
    }

    /**
     * Constructor with a custom starting grid
     *
     * @param grid The starting grid
     */
    MutableGrid(Mark[][] grid) {
        this.grid = grid;
    }


    /**
     * Check if a field is empty
     *
     * @param point which field to look at
     */
    public boolean markIsEmpty(Point point) {
        return getGrid()[point.y()][point.x()] == null;
    }

    public boolean markIsEmpty(int x, int y) {
        return getGrid()[y][x] == null;
    }

    /**
     * Check if a field has a mark
     *
     * @param point Where to look for the Mark
     * @param mark For which Mark-type to look for (self or opponent)
     */
    public boolean markIs(Point point, Mark mark) {
        return getGrid()[point.y()][point.x()] == mark;
    }

    public boolean markIs(int x, int y, Mark markType) {
        return getGrid()[y][x] == markType;
    }

    /**
     * Set an opponent Mark
     *
     * @param point Where to set the opponent Mark
     */
    public void setMarkOpponent(Point point) {
        assert (markIsEmpty(point));
        grid[point.y()][point.x()] = Mark.opponent;
    }

    /**
     * Set a self Mark
     *
     * @param point Where to set the self Mark
     */
    public void setMarkSelf(Point point) {
        assert (markIsEmpty(point));
        grid[point.y()][point.x()] = Mark.self;
    }


    @Override
    public Mark[][] getGrid() {
        return grid;
    }
}