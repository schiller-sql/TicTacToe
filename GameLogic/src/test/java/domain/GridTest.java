package domain;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static util.GridUtils.getGridFromString;

class GridTest {

    @ParameterizedTest
    @MethodSource("getAllMarkPositionsSucceedsSource")
    void getAllMarkPositionsSucceeds(Grid grid, Mark searchingMarkType, Collection<Point> expectedResult) {
        var result = grid.getAllMarkPositions(searchingMarkType);
        var assertableResult = new HashSet<>(result);

        assertEquals(expectedResult, assertableResult);
    }

    @ParameterizedTest
    @MethodSource("analyseRowsBothDirectionsMarkOrderSucceedsSource")
    void analyseRowsBothDirectionsMarkOrderSucceeds(
            Grid grid,
            boolean countBothDirectionsPoints,
            int criteriaPosition,
            Mark[] criteria,
            Collection<Point> expectedResult
    ) {
        var result = grid.analyseRowsBothDirectionsMarkOrder(countBothDirectionsPoints, criteriaPosition, criteria);

        assertEquals(expectedResult, result);
    }

    @Disabled
    @ParameterizedTest
    @MethodSource("analyseRowsRandomMarkOrderSucceedsSource")
    void analyseRowsRandomMarkOrderSucceeds(Grid grid, Collection<Point> expectedResult) {
    }

    static Stream<Arguments> getAllMarkPositionsSucceedsSource() {
        return Stream.of(
                markPositionsResult(
                        """
                                X | O | X
                                O | O | X
                                X | X | O
                                """,
                        Mark.opponent,
                        new Point(1, 0),
                        new Point(0, 1),
                        new Point(1, 1),
                        new Point(2, 2)
                ),
                markPositionsResult(
                        """
                                ∙ | ∙ | ∙
                                ∙ | ∙ | ∙
                                ∙ | ∙ | ∙
                                """,
                        Mark.self
                ),
                markPositionsResult(
                        """
                                X | O | X
                                X | X | O
                                X | O | O
                                """,
                        null
                ),
                markPositionsResult(
                        """
                                X | ∙ | O
                                ∙ | X | ∙
                                ∙ | X | O
                                """,
                        Mark.self,
                        new Point(0, 0),
                        new Point(1, 1),
                        new Point(1, 2)
                )
        );
    }

    static Stream<Arguments> analyseRowsBothDirectionsMarkOrderSucceedsSource() {
        return Stream.of(
                // Vertical
                analyseRowsBothDirectionsResult(
                        """
                                X | ∙ | X
                                X | ∙ | X
                                X | ∙ | X
                                """,
                        false,
                        1,
                        new Mark[]{Mark.self, null, Mark.self},
                        new Point(1, 0),
                        new Point(1, 1),
                        new Point(1, 2)
                ),
                analyseRowsBothDirectionsResult(
                        """
                                X | ∙ | X
                                X | ∙ | X
                                X | ∙ | X
                                """,
                        true,
                        1,
                        new Mark[]{Mark.self, null, Mark.self},
                        new Point(1, 0),
                        new Point(1, 1),
                        new Point(1, 2)
                ),
                analyseRowsBothDirectionsResult(
                        """
                                X | O | X
                                X | O | X
                                X | O | X
                                """,
                        true,
                        0,
                        new Mark[]{Mark.self, Mark.opponent, Mark.self},
                        new Point(0, 0),
                        new Point(2, 0),
                        new Point(0, 1),
                        new Point(2, 1),
                        new Point(0, 2),
                        new Point(2, 2)
                ),
                // Horizontal
                analyseRowsBothDirectionsResult(
                        """
                                X | X | X
                                ∙ | ∙ | ∙
                                X | X | X
                                """,
                        false,
                        1,
                        new Mark[]{Mark.self, null, Mark.self},
                        new Point(0, 1),
                        new Point(1, 1),
                        new Point(2, 1)
                ),
                analyseRowsBothDirectionsResult(
                        """
                                X | X | X
                                ∙ | ∙ | ∙
                                X | X | X
                                """,
                        true,
                        1,
                        new Mark[]{Mark.self, null, Mark.self},
                        new Point(0, 1),
                        new Point(1, 1),
                        new Point(2, 1)
                ),
                analyseRowsBothDirectionsResult(
                        """
                                X | X | X
                                O | O | O
                                X | X | X
                                """,
                        true,
                        0,
                        new Mark[]{Mark.self, Mark.opponent, Mark.self},
                        new Point(0, 0),
                        new Point(0, 2),
                        new Point(1, 0),
                        new Point(1, 2),
                        new Point(2, 0),
                        new Point(2, 2)
                ),
                analyseRowsBothDirectionsResult(
                        """
                                X | ∙ | ∙
                                ∙ | O | ∙
                                ∙ | ∙ | X
                                """,
                        true,
                        1,
                        new Mark[]{Mark.self, Mark.opponent, Mark.self},
                        new Point(1, 1)
                ),
                analyseRowsBothDirectionsResult(
                        """
                                X | X | X
                                O | O | ∙
                                O | O | X
                                """,
                        true,
                        1,
                        new Mark[]{Mark.self, Mark.opponent, Mark.self},
                        new Point(1, 1)
                )
        );
    }

    static Stream<Arguments> analyseRowsRandomMarkOrderSucceedsSource() {
        return Stream.of(
        );
    }

    static Arguments markPositionsResult(String rawGrid, Mark searchingMarkType, Point... expectedResult) {
        return Arguments.of(
                getGridFromString(rawGrid),
                searchingMarkType,
                new HashSet<>(Arrays.asList(expectedResult))
        );
    }

    static Arguments analyseRowsBothDirectionsResult(
            String rawGrid,
            boolean countBothDirectionsPoints,
            int criteriaPosition,
            Mark[] criteria,
            Point... expectedResult
    ) {
        assert (criteria.length == 3);
        return Arguments.of(
                getGridFromString(rawGrid),
                countBothDirectionsPoints,
                criteriaPosition,
                criteria,
                new HashSet<>(Arrays.asList(expectedResult))
        );
    }
}