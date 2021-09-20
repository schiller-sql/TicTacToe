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
     * Get three Marks in a row
     *
     * @param row Which row to get back
     * @return The marks as an array
     */
    public Mark[] getRow(Row row) {
        return new Mark[]{
                getMark(row.firstX(), row.firstY()),
                getMark(row.getMiddle()),
                getMark(row.secondX(), row.secondY()),
        };
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

    /**
     * Analyse each criterion if it matches Marks from either of both directions
     *
     * @param criteriaPosition          Which of the criteria should be looked at
     * @param countBothDirectionsPoints True means that if both the outer Marks are of the same value,
     *                                  the outer Marks positions will both be returned
     *                                  <p>
     *                                  Example: When you have opponent, self, opponent,
     *                                  if you set countBothDirectionsPoints to true,
     *                                  it means that a Point for the first and second opponent should be given,
     *                                  but setting it to false means that only the position
     *                                  of one of the opponents not both, will be returned
     * @param criteria                  Should be three long, is the marks that should be found in rows
     * @return All found Points
     * // (Not important) @apiNote If criteriaPosition is 1, make sure that both outer Marks do not equal the center mark
     */
    public Collection<Point> analyseRowsBothDirectionsMarkOrder(boolean countBothDirectionsPoints, int criteriaPosition, Mark... criteria) {
        assert (criteria.length == 3);
        assert (criteriaPosition >= 0 && criteriaPosition < 3);

        // assert criteriaPosition != 1 || (criteria[criteriaPosition] != criteria[0] && criteria[criteriaPosition] != criteria[2]);

        final Collection<Point> foundPoints = new HashSet<>();

        // alternating between i being y (even)
        // and j(Left/Right) being y (!even)
        for (int _i = 0; _i < 6; _i++) {
            final int i = _i / 2;
            final boolean even = _i % 2 == 0;

            final Mark[] row;

            if (even) {
                row = gridData[i];
            } else {
                row = new Mark[]{gridData[0][i], gridData[1][i], gridData[2][i]};
            }

            final int jLeft = rowMatchesDirectionalCriteria(criteriaPosition, true, criteria, row);
            final int jRight = rowMatchesDirectionalCriteria(criteriaPosition, false, criteria, row);

            addCorrectPointsToCollection(foundPoints, countBothDirectionsPoints, even, jLeft, jRight, i, i);
        }

        // jUpper begins at (0|0) and goes to (2|2)
        final int xUpperLeft = rowMatchesDirectionalCriteria(criteriaPosition, true, criteria, getDiagonalUpper());
        final int xUpperRight = rowMatchesDirectionalCriteria(criteriaPosition, false, criteria, getDiagonalUpper());
        addCorrectPointsToCollection(foundPoints, countBothDirectionsPoints, true, xUpperLeft, xUpperRight, xUpperLeft, xUpperRight);

        // jLower begins at (0|2) and goes to (2|0)
        final int xLowerLeft = rowMatchesDirectionalCriteria(criteriaPosition, true, criteria, getDiagonalLower());
        final int xLowerRight = rowMatchesDirectionalCriteria(criteriaPosition, false, criteria, getDiagonalLower());
        addCorrectPointsToCollection(foundPoints, countBothDirectionsPoints, true, xLowerLeft, xLowerRight, 2 - xLowerLeft, 2 - xLowerRight);

        // remove countBothDirectionsPoints
        return foundPoints;
    }

    private void addCorrectPointsToCollection(Collection<Point> collection, boolean duplicates, boolean even, int left, int right, int leftOther, int rightOther) {
        if (left != -1) {
            if (even) {
                collection.add(new Point(left, leftOther));
            } else {
                collection.add(new Point(leftOther, left));
            }
        }
        if (right != -1 && (right != left || (duplicates && left != 0))) {
            if (even) {
                collection.add(new Point(right, rightOther));
            } else {
                collection.add(new Point(rightOther, right));
            }
        }
    }

    private int rowMatchesDirectionalCriteria(int rowSearchPosition, boolean directionIsLeft, Mark[] criteria, Mark[] row) {
        int searchPosition = -1;
        for (int i = 0; i < 3; i++) {
            if (row[i] != criteria[i]) {
                return -1;
            }
            if (i == rowSearchPosition) {
                searchPosition = i;
            }
        }
        return directionIsLeft ? searchPosition : 2 - searchPosition;
    }

    /**
     * @return The row from (0|0) to (2|2)
     */
    private Mark[] getDiagonalUpper() {
        return new Mark[]{gridData[0][0], gridData[1][1], gridData[2][2]};
    }

    /**
     * @return The row from (0|2) to (2|0)
     */
    private Mark[] getDiagonalLower() {
        return new Mark[]{gridData[2][0], gridData[1][1], gridData[0][2]};
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
