package opponent;

import domain.Grid;
import domain.Mark;
import domain.Point;
import fixtures.TestableAdvancedBaseOpponent;
import opponent.base_opponents.AdvancedBaseOpponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static util.GridUtils.getGridFromString;

class AdvancedBaseOpponentTest {
    AdvancedBaseOpponent advancedBaseOpponent;

    @BeforeEach
    void init() {
        advancedBaseOpponent = new TestableAdvancedBaseOpponent();
    }

    @MethodSource("getRowFinalNeededPointsSource")
    @ParameterizedTest
    void getRowFinalNeededPoints(Grid grid, Point[] finalNeededPoints) {
        final Point[] foundPoints = advancedBaseOpponent.getRowFinalNeededPoints(grid, Mark.opponent);

        assertArrayEquals(finalNeededPoints, foundPoints);
    }

    private static Stream<Arguments> getRowFinalNeededPointsSource() {
        return Stream.of(
                finalNeededPointsScenario(
                        """
                                O | ∙ | O
                                ∙ | ∙ | ∙
                                ∙ | ∙ | ∙
                                """,
                        new int[][]{
                                {1, 0},
                        }
                ),
                finalNeededPointsScenario(
                        """
                                ∙ | ∙ | O
                                ∙ | ∙ | ∙
                                O | ∙ | ∙
                                """,
                        new int[][]{
                                {1, 1},
                        }
                ),
                finalNeededPointsScenario(
                        """
                                O | ∙ | ∙
                                O | O | ∙
                                O | ∙ | ∙
                                """,
                        new int[][]{
                                {2, 1},
                                {2, 2},
                                {2, 0},
                        }
                ),
                finalNeededPointsScenario(
                        """
                                X | O | ∙
                                O | X | O
                                X | X | ∙
                                """,
                        new int[][]{}
                )
        );
    }

    static Arguments finalNeededPointsScenario(String grid, int[][] rawFinalNeededPoints) {
        final Point[] finalNeededPoints = new Point[rawFinalNeededPoints.length];
        for (int i = 0; i < rawFinalNeededPoints.length; i++) {
            assert (rawFinalNeededPoints[i].length == 2);
            finalNeededPoints[i] = new Point(rawFinalNeededPoints[i][0], rawFinalNeededPoints[i][1]);
        }
        return Arguments.of(
                getGridFromString(grid),
                finalNeededPoints
        );
    }
}