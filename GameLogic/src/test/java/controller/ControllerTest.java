package controller;

import domain.Mark;
import domain.Point;
import fixtures.FakeOpponent;
import opponent.Opponent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ControllerTest {

    @Test
    void setPointDirectlyWins() {
        final Mark[][] grid = getGrid("""
                O | X | ∙
                X | ∙ | X
                X | X | ∙
                """);
        final GameController controller = new GameController(new FakeOpponent(), grid);
        final Point placePoint = new Point(1, 1);

        controller.setPoint(placePoint);
        final GameState gameState = controller.getState();

        assertEquals(gameState, GameState.won);
    }

    @Test
    void setPointLoses() {
        final Opponent opponent = new FakeOpponent(new Point(2, 2));
        final Mark[][] grid = getGrid("""
                O | X | ∙
                X | O | X
                X | X | ∙
                """);
        final GameController controller = new GameController(opponent, grid);
        final Point placePoint = new Point(2, 0);

        controller.setPoint(placePoint);
        final GameState gameState = controller.getState();

        assertEquals(gameState, GameState.lost);
    }

    @MethodSource("constructorCheckDeterminesCorrectStateSource")
    @ParameterizedTest
    void constructorCheckDeterminesCorrectState(Mark[][] testGrid, GameState expected) {
        final GameController controller = new GameController(new FakeOpponent(), testGrid);
        final GameState gameState = controller.getState();
        assertEquals(gameState, expected);
    }

    static Stream<Arguments> constructorCheckDeterminesCorrectStateSource() {
        return Stream.of(
                // running
                gameControllerResult(
                        """
                                ∙ | X | ∙
                                ∙ | ∙ | ∙
                                ∙ | X | ∙
                                """,
                        GameState.running
                ),
                gameControllerResult(
                        """
                                O | X | O
                                O | X | O
                                X | O | ∙
                                """,
                        GameState.running
                ),
                gameControllerResult(
                        """
                                X | O | O
                                O | ∙ | O
                                O | O | X
                                """,
                        GameState.running
                ),
                gameControllerResult(
                        """
                                ∙ | ∙ | ∙
                                ∙ | ∙ | ∙
                                ∙ | ∙ | ∙
                                """,
                        GameState.running
                ),
                // winning
                gameControllerResult(
                        """
                                ∙ | X | ∙
                                ∙ | X | ∙
                                ∙ | X | ∙
                                """,
                        GameState.won
                ),
                gameControllerResult(
                        """
                                ∙ | X | ∙
                                ∙ | X | ∙
                                ∙ | X | ∙
                                """,
                        GameState.won
                ),
                gameControllerResult(
                        """
                                X | O | O
                                O | X | O
                                O | O | X
                                """,
                        GameState.won
                ),
                gameControllerResult(
                        """
                                O | O | X
                                O | X | O
                                X | O | O
                                """,
                        GameState.won
                ),
                // losing
                gameControllerResult(
                        """
                                O | X | O
                                X | O | X
                                O | X | O
                                """,
                        GameState.lost
                ),
                gameControllerResult(
                        """
                                O | O | O
                                X | O | X
                                ∙ | X | ∙
                                """,
                        GameState.lost
                ),
                gameControllerResult(
                        """
                                O | O | O
                                O | O | O
                                O | O | O
                                """,
                        GameState.lost
                ),
                gameControllerResult(
                        """
                                O | O | X
                                O | O | O
                                X | O | O
                                """,
                        GameState.lost
                ),
                // tying
                gameControllerResult(
                        """
                                O | O | X
                                X | X | O
                                O | X | O
                                """,
                        GameState.tie
                ),
                gameControllerResult(
                        """
                                O | X | O
                                X | X | O
                                X | O | X
                                """,
                        GameState.tie
                ),
                gameControllerResult(
                        """
                                X | O | X
                                X | O | O
                                O | X | X
                                """,
                        GameState.tie
                ),
                gameControllerResult(
                        """
                                X | O | X
                                O | O | X
                                X | X | O
                                """,
                        GameState.tie
                )
        );
    }

    static Arguments gameControllerResult(String grid, GameState state) {
        return Arguments.of(
                getGrid(grid),
                state
        );
    }

/*
Alternativ solution would be one method with array-streams and lambda
Expressions instead of three methods
*/
    static Mark[][] getGrid(String stringGrid) {
        assert (stringGrid.length() == 30);
        final String[] rawGrid = stringGrid.split("\n");
        final Mark[][] grid = new Mark[3][3];
        for (int i = 0; i < 3; i++) {
            grid[i] = marksFromStringLine(rawGrid[i]);
        }
        return grid;
    }

    static Mark[] marksFromStringLine(String line) {
        final String[] rawMarks = line.trim().split("\\s\\|\\s");
        final Mark[] marks = new Mark[3];
        for (int i = 0; i < 3; i++) {
            marks[i] = inferMark(rawMarks[i].charAt(0));
        }
        return marks;
    }

    static Mark inferMark(char stringMark) {
        return switch (stringMark) {
            case 'X' -> Mark.self;
            case 'O' -> Mark.opponent;
            case '∙' -> null;
            default -> throw new Error("Not a valid Mark");
        };
    }

}