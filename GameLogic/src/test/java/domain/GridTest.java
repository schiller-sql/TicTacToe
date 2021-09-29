package domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static util.GridUtils.getGridFromString;

class GridTest {

    @ParameterizedTest
    @MethodSource("getAllMarkPositionsSucceedsSource")
    void getAllMarkPositionsSucceeds(Grid grid, Mark searchingMarkType, Set<Point> expectedResult) {
        var result = grid.getAllMarkPositions(searchingMarkType);

        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @MethodSource("analyseRowsBothDirectionsMarkOrderSucceedsSource")
    void analyseRowsBothDirectionsMarkOrderSucceeds(
            Grid grid,
            boolean countBothDirectionsPoints,
            int criteriaPosition,
            Mark[] criteria,
            Set<Point> expectedResult
    ) {
        var result = grid.analyseRowsBothDirectionsMarkOrder(countBothDirectionsPoints, criteriaPosition, criteria);

        assertEquals(expectedResult, result);
    }


    @ParameterizedTest
    @MethodSource("analyseRowsRandomMarkOrderForOnePointSucceedsSource")
    void analyseRowsRandomMarkOrderForOnePointSucceeds(
            Grid grid,
            Mark searchedMark,
            Mark patternMark2,
            Mark patternMark3,
            Set<Point> expectedResult
    ) {
        var result = grid.analyseRowsRandomMarkOrderForOnePoint(searchedMark, patternMark2, patternMark3);

        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @MethodSource("analyseRowsRandomMarkOrderSucceedsSource")
    void analyseRowsRandomMarkOrderSucceeds(Grid grid, Mark[] criteria, Set<Row> expectedResult) {
        var result = grid.analyseRowsRandomMarkOrder(criteria);

        assertEquals(expectedResult, result);
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

    static Stream<Arguments> analyseRowsRandomMarkOrderForOnePointSucceedsSource() {
        return Stream.of(
                analyseRowsRandomMarkOrderForOnePointResult(
                        """
                                ∙ | ∙ | ∙
                                ∙ | X | ∙
                                ∙ | ∙ | ∙
                                """,
                        null, Mark.opponent, Mark.opponent
                ),
                analyseRowsRandomMarkOrderForOnePointResult(
                        """
                                X | O | X
                                X | O | O
                                X | X | X
                                """,
                        Mark.opponent, Mark.self, Mark.self,
                        new Point(1, 0),
                        new Point(1, 1),
                        new Point(2, 1)
                ),
                analyseRowsRandomMarkOrderForOnePointResult(
                        """
                                ∙ | X | O
                                O | ∙ | X
                                X | O | ∙
                                """,
                        null, Mark.self, Mark.opponent,
                        new Point(0, 0),
                        new Point(1, 1),
                        new Point(2, 2)
                ),
                analyseRowsRandomMarkOrderForOnePointResult(
                        """
                                ∙ | ∙ | ∙
                                ∙ | ∙ | ∙
                                ∙ | ∙ | ∙
                                """,
                        Mark.opponent, null, null
                ),
                analyseRowsRandomMarkOrderForOnePointResult(
                        """
                                ∙ | ∙ | ∙
                                ∙ | X | X
                                ∙ | X | X
                                """,
                        null, Mark.self, Mark.self,
                        // (0|0)
                        new Point(0, 0),
                        // (1|0)-(2|0)
                        new Point(1, 0),
                        new Point(2, 0),
                        // (0|1)-(0|2)
                        new Point(0, 1),
                        new Point(0, 2)
                )
        );
    }

    static Stream<Arguments> analyseRowsRandomMarkOrderSucceedsSource() {
        return Stream.of(
                analyseRowsRandomMarkOrderResult(
                        """
                                X | ∙ | O
                                ∙ | O | ∙
                                O | ∙ | X
                                """,
                        new Mark[]{Mark.self, Mark.self, Mark.opponent},
                        new Point(0, 0), new Point(2, 2)
                ),
                analyseRowsRandomMarkOrderResult(
                        """
                                O | X | X
                                X | X | O
                                X | O | O
                                """,
                        new Mark[]{Mark.self, Mark.self, Mark.opponent},
                        // Horizontal
                        new Point(0, 0), new Point(2, 0),
                        new Point(0, 1), new Point(2, 1),
                        // Vertical
                        new Point(0, 0), new Point(0, 2),
                        new Point(1, 0), new Point(1, 2)
                ),
                analyseRowsRandomMarkOrderResult(
                        """
                                ∙ | X | O
                                O | ∙ | X
                                X | O | ∙
                                """,
                        new Mark[]{Mark.self, Mark.opponent, null},
                        // Horizontal
                        new Point(0, 0), new Point(2, 0),
                        new Point(0, 1), new Point(2, 1),
                        new Point(0, 2), new Point(2, 2),
                        // Vertical
                        new Point(0, 0), new Point(0, 2),
                        new Point(1, 0), new Point(1, 2),
                        new Point(2, 0), new Point(2, 2),
                        // Diagonal
                        new Point(0, 2), new Point(2, 0)
                ),
                analyseRowsRandomMarkOrderResult(
                        """
                                ∙ | ∙ | ∙
                                ∙ | ∙ | ∙
                                ∙ | ∙ | ∙
                                """,
                        new Mark[]{Mark.opponent, null, null}
                ),
                analyseRowsRandomMarkOrderResult(
                        """
                                ∙ | ∙ | ∙
                                ∙ | X | X
                                ∙ | X | X
                                """,
                        new Mark[]{null, Mark.self, Mark.self},
                        // (0|0)
                        new Point(0, 0), new Point(2, 2),
                        // (1|0)-(2|0)
                        new Point(1, 0), new Point(1, 2),
                        new Point(2, 0), new Point(2, 2),
                        // (0|1)-(0|2)
                        new Point(0, 1), new Point(2, 1),
                        new Point(0, 2), new Point(2, 2)
                )
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

    static Arguments analyseRowsRandomMarkOrderForOnePointResult(String rawGrid, Mark searchedMark, Mark patternMark2, Mark patternMark3, Point... expectedResult) {
        return Arguments.of(
                getGridFromString(rawGrid),
                searchedMark,
                patternMark2,
                patternMark3,
                new HashSet<>(Arrays.asList(expectedResult))
        );
    }

    static Arguments analyseRowsRandomMarkOrderResult(String rawGrid, Mark[] criteria, Point... rawExpectedResult) {
        /*
        two Points represent one Row, which is why they are formatted on the same line,
        and why there has to be an equal amount of them
        */
        assert (rawExpectedResult.length % 2 == 0);

        var expectedResult = new HashSet<Row>();
        for (int i = 0; i < rawExpectedResult.length; i += 2) {
            expectedResult.add(Row.fromTwoPoints(rawExpectedResult[i], rawExpectedResult[i + 1]));
        }
        return Arguments.of(
                getGridFromString(rawGrid),
                criteria,
                expectedResult
        );
    }
}