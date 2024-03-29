package domain;

import java.lang.reflect.Array;
import java.util.*;


/**
 * Represents a 3 by 3 tic-tac-toe grid
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
     * @return All found positions as a Point Set
     */
    public Set<Point> getAllMarkPositions(Mark type) {
        final Set<Point> foundPoints = new HashSet<>();
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
    public Set<Point> analyseRowsBothDirectionsMarkOrder(boolean countBothDirectionsPoints, int criteriaPosition, Mark... criteria) {
        assert (criteria.length == 3);
        assert (criteriaPosition >= 0 && criteriaPosition < 3);

        // assert criteriaPosition != 1 || (criteria[criteriaPosition] != criteria[0] && criteria[criteriaPosition] != criteria[2]);

        final Set<Point> foundPoints = new HashSet<>();

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

            addCorrectPointsToSet(foundPoints, countBothDirectionsPoints, even, jLeft, jRight, i, i);
        }

        // jUpper begins at (0|0) and goes to (2|2)
        final int xUpperLeft = rowMatchesDirectionalCriteria(criteriaPosition, true, criteria, getDiagonalUpper());
        final int xUpperRight = rowMatchesDirectionalCriteria(criteriaPosition, false, criteria, getDiagonalUpper());
        addCorrectPointsToSet(foundPoints, countBothDirectionsPoints, true, xUpperLeft, xUpperRight, xUpperLeft, xUpperRight);

        // jLower begins at (0|2) and goes to (2|0)
        final int xLowerLeft = rowMatchesDirectionalCriteria(criteriaPosition, true, criteria, getDiagonalLower());
        final int xLowerRight = rowMatchesDirectionalCriteria(criteriaPosition, false, criteria, getDiagonalLower());
        addCorrectPointsToSet(foundPoints, countBothDirectionsPoints, true, xLowerLeft, xLowerRight, 2 - xLowerLeft, 2 - xLowerRight);

        // remove countBothDirectionsPoints
        return foundPoints;
    }

    private void addCorrectPointsToSet(Set<Point> pointsSet, boolean duplicates, boolean even, int left, int right, int leftOther, int rightOther) {
        if (left != -1) {
            if (even) {
                pointsSet.add(new Point(left, leftOther));
            } else {
                pointsSet.add(new Point(leftOther, left));
            }
        }
        if (right != -1 && (right != left || (duplicates && left != 0))) {
            if (even) {
                pointsSet.add(new Point(right, rightOther));
            } else {
                pointsSet.add(new Point(rightOther, right));
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
     * Get one Point back from all rows,
     * which contain the Marks in some order which are searched for
     *
     * @param searchedMark Which mark should be searched for and the position given
     * @param patternMark2 Other mark to be searched for
     * @param patternMark3 Other mark to be searched for
     * @return All positions of searchedMark
     * @apiNote searchedMark should not equal either patternMark2 or patternMark3
     */
    public Set<Point> analyseRowsRandomMarkOrderForOnePoint(Mark searchedMark, Mark patternMark2, Mark patternMark3) {
        assert (searchedMark != patternMark2);
        assert (searchedMark != patternMark3);

        final Set<Point> foundRows = new HashSet<>();
        // alternating between j being y (even)
        // and j being x (!even)
        for (int _j = 0; _j < 6; _j++) {
            final int j = _j / 2;
            final boolean even = _j % 2 == 0;
            final Mark[] row;
            // even means that j is y
            if (even) {
                row = gridData[j];
            } else {
                row = new Mark[]{gridData[0][j], gridData[1][j], gridData[2][j]};
            }
            final int markPosition = rowMarkCriteriaRandomPosition(row, searchedMark, patternMark2, patternMark3);
            if (markPosition != -1) {
                // even means that j is y
                if (even) {
                    foundRows.add(new Point(markPosition, j));
                } else {
                    foundRows.add(new Point(j, markPosition));
                }
            }
        }
        int upperDiagonalMarkPosition = rowMarkCriteriaRandomPosition(getDiagonalUpper(), searchedMark, patternMark2, patternMark3);
        int lowerDiagonalMarkPosition = rowMarkCriteriaRandomPosition(getDiagonalLower(), searchedMark, patternMark2, patternMark3);

        if (upperDiagonalMarkPosition != -1) {
            foundRows.add(new Point(upperDiagonalMarkPosition, upperDiagonalMarkPosition));
        }
        if (lowerDiagonalMarkPosition != -1) {
            foundRows.add(new Point(lowerDiagonalMarkPosition, 2 - lowerDiagonalMarkPosition));
        }
        return foundRows;
    }

    private int rowMarkCriteriaRandomPosition(Mark[] row, Mark searchedMark, Mark patternMark2, Mark patternMark3) {
        boolean patternTwoFound = false, patternThreeFound = false;
        int searchedMarkPosition = -1;
        for (int i = 0; i < 3; i++) {
            final Mark mark = row[i];
            if (mark == searchedMark && searchedMarkPosition == -1) {
                searchedMarkPosition = i;
            } else if (mark == patternMark2 && !patternTwoFound) {
                patternTwoFound = true;
            } else if (mark == patternMark3 && !patternThreeFound) {
                patternThreeFound = true;
            } else {
                return -1;
            }
        }
        return searchedMarkPosition;
    }

    /**
     * Same as analyseRowsRandomMarkOrderForOnePoint,
     * but gives back the Points of two of the searched for Marks in a single Row
     *
     * @param searchedMark1 The first Mark that the Point is given back for
     * @param searchedMark2 The second Mark that the Point is given back for
     * @param patternMark   The only Mark that is not given back as a Point
     * @return All Points of the Rows that match searchedMark1 or searchedMark2
     * @apiNote searchedMark1 or searchedMark2 should not be equal to patternMark3
     */
    public Set<Point> analyseRowsRandomMarkOrderForTwoPoints(Mark searchedMark1, Mark searchedMark2, Mark patternMark) {
        assert (patternMark != searchedMark1);
        assert (patternMark != searchedMark2);

        final Set<Row> foundRows = analyseRowsRandomMarkOrder(searchedMark1, searchedMark2, patternMark);
        final Set<Point> foundPoints = new HashSet<>();

        for (Row foundRow : foundRows) {
            for (Point point : foundRow.toPoints()) {
                if (getMark(point) == searchedMark1 || getMark(point) == searchedMark2) {
                    foundPoints.add(point);
                }
            }
        }
        return foundPoints;
    }

    /**
     * Same as analyseRowsRandomMarkOrderForOnePoint and analyseRowsRandomMarkOrderForTwoPoints,
     * but gives all Points of all found Rows back
     *
     * @param criteria The three Marks that should be searched for in a Row
     * @return All Points of each found Row
     * @apiNote All searchedMarks are allowed to be the same
     */
    public Set<Point> analyseRowsRandomMarkOrderForThreePoints(Mark... criteria) {
        assert (criteria.length == 3);

        final Set<Row> foundRows = analyseRowsRandomMarkOrder(criteria[0], criteria[1], criteria[2]);
        final Set<Point> foundPoints = new HashSet<>();

        for (Row foundRow : foundRows) {
            foundPoints.addAll(Arrays.stream(foundRow.toPoints()).toList());
        }
        return foundPoints;
    }

    /**
     * Searches for rows that include the Marks in criteria in a random order
     *
     * @param criteria Exactly three marks which represent, which Marks a row should contain * @return All found Rows with the criteria
     */
    public Set<Row> analyseRowsRandomMarkOrder(Mark... criteria) {
        assert (criteria.length == 3);

        final Set<Row> foundRows = new HashSet<>();
        // alternating between j being y (even)
        // and j being x (!even)
        for (int _j = 0; _j < 6; _j++) {
            final int j = _j / 2;
            final boolean even = _j % 2 == 0;
            final Mark[] row;
            // even means that j is y
            if (even) {
                row = gridData[j];
            } else {
                row = new Mark[]{gridData[0][j], gridData[1][j], gridData[2][j]};
            }
            if (rowContainsMarksInRandomPosition(row, criteria)) {
                // even means that j is y
                if (even) {
                    foundRows.add(new Row(0, j, 2, j));
                } else {
                    foundRows.add(new Row(j, 0, j, 2));
                }
            }
        }
        if (rowContainsMarksInRandomPosition(getDiagonalUpper(), criteria)) {
            foundRows.add(new Row(0, 0, 2, 2));
        }
        if (rowContainsMarksInRandomPosition(getDiagonalLower(), criteria)) {
            foundRows.add(new Row(0, 2, 2, 0));
        }
        return foundRows;
    }

    private boolean rowContainsMarksInRandomPosition(Mark[] row, Mark[] pattern) {
        final boolean[] patternMarkChecker = new boolean[3];
        row:
        for (int i = 0; i < 3; i++) {
            final Mark matchingFieldMark = row[i];
            for (int j = 0; j < 3; j++) {
                if (matchingFieldMark == pattern[j] && !patternMarkChecker[j]) {
                    patternMarkChecker[j] = true;
                    continue row;
                }
            }
            return false;
        }
        return true;
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

    public String toString(String padding) {
        StringBuilder stringBuffer = new StringBuilder();
        for (int y = 0; y < 3; y++) {
            stringBuffer.append(padding);
            for (int x = 0; x < 3; x++) {
                final Mark mark = this.getMark(x, y);
                final String markAsString = mark == null ? " " : mark == Mark.self ? "X" : "O";
                stringBuffer
                        .append(markAsString)
                        .append("|");
            }
            stringBuffer
                    .deleteCharAt(stringBuffer.length() - 1)
                    .append("\n");
        }
        return stringBuffer.toString();
    }

    public String asString(String padding) {
        StringBuilder stringBuffer = new StringBuilder();
        for (int y = 0; y < 3; y++) {
            if(y > 0) {
                stringBuffer.append(padding);
            }
            for (int x = 0; x < 3; x++) {
                final Mark mark = this.getMark(x, y);
                final String markAsString = mark == null ? "-" : mark == Mark.self ? "X" : "O";
                stringBuffer
                        .append(markAsString)
                        .append("|");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            stringBuffer.append("\n");
        }
        return stringBuffer.toString();
    }
}
