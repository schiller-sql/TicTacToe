package domain;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * Represents a 3 by 3 tic-tac-toe grid
 */
public class Grid {

    private final Mark[][] grid;

    /**
     * Empty constructor, with a clean grid
     */
    public Grid() {
        grid = new Mark[3][3];
    }

    /**
     * Constructor with a custom starting grid
     *
     * @param grid The starting grid
     */
    public Grid(Mark[][] grid) {
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
     * Get a mutated Grid,
     * which is mutated on one location with a new mark
     *
     * @param grid  Which Grid should be mutated from
     * @param point Where to place the mark
     * @param mark  The mark to place
     * @return The mutated grid
     */
    static public Grid mutatedGridFrom(Grid grid, Point point, Mark mark) {
        Mark[][] rawGrid = new Mark[3][3];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (point.x() == x && point.y() == y) {
                    rawGrid[y][x] = mark;
                } else {
                    rawGrid[y][x] = grid.getMark(x, y);
                }
            }
        }
        return new Grid(rawGrid);
    }
}