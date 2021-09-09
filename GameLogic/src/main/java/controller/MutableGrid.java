package controller;

import domain.Grid;
import domain.Mark;
import domain.Point;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Implements the Grid interface,
 * but has extra methods to 'mutate' the Grid
 */
public class MutableGrid implements Grid {

    private final Mark[][] grid;

    /**
     * Empty constructor, with a clean grid
     */
    public MutableGrid() {
        grid = new Mark[3][3];
    }

    /**
     * Constructor with a custom starting grid
     *
     * @param grid The starting grid
     */
    public MutableGrid(Mark[][] grid) {
        this.grid = grid;
    }


    /**
     * Check if a field is empty by its Point
     *
     * @param point Which field to look at
     * @return True if the point is empty
     */
    public boolean markIsEmpty(Point point) {
        return grid[point.y()][point.x()] == null;
    }

    /**
     * Check if a field is empty by its coordinates
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return True if the point is empty
     */
    public boolean markIsEmpty(int x, int y) {
        return grid[y][x] == null;
    }

    /**
     * Get a Mark in the grid by its Point
     *
     * @param point The Point at which the mark is at
     * @return The Mark which is searched for
     */
    public Mark getMark(Point point) {
        return grid[point.y()][point.x()];
    }

    /**
     * Get all positions of a mark type in this Grid
     *
     * @param type The type of Mark to look for,
     *             null means looking for an empty field
     * @return All positions as a Point array
     */
    // TODO: Fix the warning by IntelliJ
    public Point[] getAllOfMarkType(Mark type) {
        ArrayList<Point> foundPoints = new ArrayList<>();
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (grid[y][x] == type) {
                    foundPoints.add(new Point(x, y));
                }
            }
        }
        return foundPoints.toArray((Point[]) Array.newInstance(Point.class, foundPoints.size()));
    }

    /**
     * Get a Mark in the grid by its coordinates
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return The Mark which is searched for
     */
    public Mark getMark(int x, int y) {
        return grid[y][x];
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
}
