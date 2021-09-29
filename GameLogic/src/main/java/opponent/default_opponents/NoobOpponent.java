package opponent.default_opponents;

import domain.Grid;
import domain.Mark;
import domain.Point;
import opponent.Opponent;

import java.util.*;

/**
 * Tries to make the player win
 */
public class NoobOpponent extends Opponent {
    @Override
    public Point move(Grid grid) {
        final Set<Point> twoMarksPlacedFilter = grid.analyseRowsRandomMarkOrderForOnePoint(null, Mark.self, Mark.self);
        final Set<Point> oneMarkPlacedFilter = grid.analyseRowsRandomMarkOrderForTwoPoints(null, null, Mark.self);
        final Set<Point> twoMarksPlacedSelfFilter = grid.analyseRowsRandomMarkOrderForOnePoint(null, Mark.opponent, Mark.opponent);

        final Set<Point> nonFiltered = filterEmptyPoints(grid);
        final Set<Point> filtered1 = filterEmptyPoints(grid, oneMarkPlacedFilter);
        final Set<Point> filtered2 = filterEmptyPoints(grid, twoMarksPlacedFilter);
        final Set<Point> filtered3 = filterEmptyPoints(grid, oneMarkPlacedFilter, twoMarksPlacedSelfFilter);
        final Set<Point> filtered4 = filterEmptyPoints(grid, twoMarksPlacedFilter, twoMarksPlacedSelfFilter);
        final Set<Point> filtered5 = filterEmptyPoints(grid, oneMarkPlacedFilter, twoMarksPlacedFilter, twoMarksPlacedSelfFilter);
        final Set<Point> nonWinnablePoints = grid.analyseRowsRandomMarkOrderForOnePoint(null, Mark.self, Mark.opponent);

        final Set<Point>[] allPossibilities = new Set[]{
                nonFiltered,
                filtered1,
                filtered2,
                filtered3,
                filtered4,
                filtered5,
                nonWinnablePoints,
        };

        int bestPossibility = 0;
        for (int i = 1; i < allPossibilities.length; i++) {
            if (allPossibilities[i].size() > 0) {
                bestPossibility = i;
            }
        }

        return getBestPointFromSet(allPossibilities[bestPossibility]);
    }

    /**
     * @return The best Point, it prioritizes in no middle and
     * then no 'straights' (the ones that directly touch (1|1)
     */
    private Point getBestPointFromSet(Set<Point> pointSet) {
        final Set<Point> editablePointsSet = new HashSet<>(pointSet);

        Point lastPoint = getHighestPointFromSet(editablePointsSet);

        editablePointsSet.removeAll(middle());
        if (editablePointsSet.size() == 0) {
            return lastPoint;
        }
        lastPoint = getHighestPointFromSet(editablePointsSet);

        editablePointsSet.removeAll(straights());
        if (editablePointsSet.size() == 0) {
            return lastPoint;
        }
        return getHighestPointFromSet(editablePointsSet);
    }

    /**
     * @return Point closest to (2|2)
     */
    private Point getHighestPointFromSet(Set<Point> points) {
        Point highestPoint = null;
        for (Point point : points) {
            if (highestPoint == null) {
                highestPoint = point;
            } else if (point.hashCode() > highestPoint.hashCode()) {
                highestPoint = point;
            }
        }
        return highestPoint;
    }

    /**
     * @return Filter points from a Set,
     * that contains all Points which are possible to place on
     */
    @SafeVarargs
    private Set<Point> filterEmptyPoints(Grid grid, Collection<Point>... filters) {
        final Set<Point> basePoints = grid.getAllMarkPositions(null);
        for (Collection<Point> filter : filters) {
            basePoints.removeAll(filter);
        }
        return basePoints;
    }

    /**
     * For filtering out (1|1)
     */
    private Collection<Point> middle() {
        return Collections.singletonList(new Point(1, 1));
    }

    /**
     * For filtering out all Points touching (1|1)
     */
    private Collection<Point> straights() {
        return Arrays.stream(new Point[]{
                new Point(1, 0),
                new Point(1, 2),
                new Point(0, 1),
                new Point(2, 1),
        }).toList();
    }

    /**
     * For filtering out all diagonals
     */
    private Collection<Point> diagonals() {
        return Arrays.stream(new Point[]{
                new Point(0, 0),
                new Point(2, 0),
                new Point(2, 2),
                new Point(0, 2),
                new Point(1, 1)
        }).toList();
    }
}