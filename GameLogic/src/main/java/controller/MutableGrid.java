package controller;

import domain.Grid;
import domain.Mark;
import domain.Point;

/**
 * Mutable version of the Grid class,
 * so the controller can edit the grid
 *
 * Should only be used in the controller,
 * as in the opponent for example,
 * it returns the point to be set
 */
class MutableGrid implements Grid {

    private final Mark[][] grid;

    MutableGrid() {
        grid = new Mark[3][3];
    }

    MutableGrid(Mark[][] grid) {
        this.grid = grid;
    }

    public void setMarkOpponent(Point point) {
        assert(markIsEmpty(point));
        grid[point.x()][point.y()] = Mark.opponent;
    }

    public void setMarkSelf(Point point) {
        assert(markIsEmpty(point));
        grid[point.x()][point.y()] = Mark.self;
    }

    @Override
    public Mark[][] getGrid() {
        return grid;
    }
}