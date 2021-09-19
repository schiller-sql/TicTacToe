package domain;

import java.lang.reflect.Array;
import java.util.*;


/**
 * Represents a 3 by 3 tic-tac-toe gridData
 */
public final class Grid {
    private final Mark[][] gridData;

    /**
     * Get a grid from some base data
     */
    public Grid(Mark[][] gridData) {
        this.gridData = gridData;
    }

    /**
     * Get an empty Grid
     */
    public Grid() {
        this.gridData = new Mark[3][3];
    }

    /**
     * Check if a field is empty by its Point
     *
     * @param point Which field to look at
     * @return True if the point is empty
     */
    public boolean markIsEmpty(Point point) {
        return gridData[point.y()][point.x()] == null;
    }

    /**
     * Check if a field is empty by its coordinates
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return True if the point is empty
     */
    public boolean markIsEmpty(int x, int y) {
        return gridData[y][x] == null;
    }

    /**
     * Get a Mark in the gridData by its Point
     *
     * @param point The Point at which the mark is at
     * @return The Mark which is searched for
     */
    public Mark getMark(Point point) {
        return gridData[point.y()][point.x()];
    }


    /**
     * Get a Mark in the gridData by its coordinates
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return The Mark which is searched for
     */
    public Mark getMark(int x, int y) {
        return gridData[y][x];
    }

    /**
     * Get a mutated Grid,
     * which is mutated from this gridData,
     * on one location with a new mark
     *
     * @param point The location to place the mark
     * @param mark  The mark to place
     * @return The mutated gridData
     */
    public Grid copyWith(Point point, Mark mark) {
        Mark[][] rawGrid = new Mark[3][3];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (point.x() == x && point.y() == y) {
                    rawGrid[y][x] = mark;
                } else {
                    rawGrid[y][x] = getMark(x, y);
                }
            }
        }
        return new Grid(rawGrid);
    }

    /**
     * Get all positions of a mark type in this Grid
     *
     * @param type The type of Mark to look for,
     *             null means looking for empty fields
     * @return All found positions as a Point array
     * @deprecated Use getAllMarkPositions instead
     */
    @Deprecated
    public Point[] getAllOfMarkType(Mark type) {
        ArrayList<Point> foundPoints = new ArrayList<>();
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (gridData[y][x] == type) {
                    foundPoints.add(new Point(x, y));
                }
            }
        }
        return foundPoints.toArray((Point[]) Array.newInstance(Point.class, foundPoints.size()));
    }

    /**
     * Get all positions of a mark type in this Grid
     *
     * @param type The type of Mark to look for,
     *             null means looking for empty fields
     * @return All found positions as a Point Collection
     */
    public Collection<Point> getAllMarkPositions(Mark type) {
        final Collection<Point> foundPoints = new ArrayList<>();
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (gridData[y][x] == type) {
                    foundPoints.add(new Point(x, y));
                }
            }
        }
        return foundPoints;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Grid) obj;
        return Arrays.deepEquals(this.gridData, that.gridData);
    }

    @Override
    public int hashCode() {
        return Objects.hash((Object) gridData);
    }

    @Override
    public String toString() {
        return Arrays.deepToString(gridData);
    }


    /**
     * Factory constructor to get an empty Grid
     *
     * @deprecated Use the new empty constructor
     */
    @Deprecated
    static public Grid emptyGrid() {
        return new Grid(new Mark[3][3]);
    }

}
