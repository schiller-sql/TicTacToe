package opponent.default_opponents;

import controller.GameController;
import controller.GameState;
import domain.Grid;
import domain.Point;
import opponent.Opponent;
import opponent.OpponentTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static util.GridUtils.getGridFromString;

class GodOpponentTest extends OpponentTest {

    @Test
    void move1() { //generates a simple sized Tree
        final var grid = getGridFromString( //check
                """
                        X | X | O
                        X | ∙ | O
                        ∙ | O | ∙
                        """
        ); //Output should be: 2,2

        final var result = opponent.move(grid);

        assertEquals(new Point(2, 2), result);
    }

    @Test
    void move2() { //generates a simple sized Tree
        final var grid = getGridFromString( //check
                """
                        O | O | ∙
                        O | X | X
                        X | ∙ | X
                        """
        ); //Output should be: 2,0

        final var result = opponent.move(grid);

        assertEquals(new Point(2, 0), result);
    }

    @Test
    void move3() { //generates a simple sized Tree
        final var grid = getGridFromString( //check
                """
                        O | X | X
                        O | O | X
                        ∙ | X | ∙
                        """
        ); //Output should be: 2,2 (0,2 has the same score)

        final var result = opponent.move(grid);

        assertEquals(new Point(2, 2), result);
    }

    @Test
    void move4() { //generates a moderate sized Tree
        final var grid = getGridFromString( //check
                """
                        X | ∙ | ∙
                        ∙ | O | X
                        X | ∙ | O
                        """
        ); //Output should be: 0,1
        final var result = opponent.move(grid);

        assertEquals(new Point(0, 1), result);
    }

    @Test
    void move5() { //generates a moderate sized Tree
        final var grid = getGridFromString( //check
                """
                        X | ∙ | ∙
                        O | O | X
                        X | ∙ | ∙
                        """
        ); //Output should be: 0,1

        final var result = opponent.move(grid);

        assertEquals(new Point(2, 2), result);
    }

    @Test
    void move6() { //generates a moderate sized Tree
        final var grid = getGridFromString( //check
                """
                        X | ∙ | ∙
                        ∙ | X | ∙
                        X | O | O
                        """
        ); //Output should be: 0,1

        final var result = opponent.move(grid);

        assertEquals(new Point(2, 1), result);
    }

    @Override
    protected Opponent getOpponent() {
        return new GodOpponent();
    }


    @Test
    public void alwaysWinsOrTies() {
        callOtherMoves(new Grid(), new Grid[0]);
    }

    private void makeOneMove(Point point, Grid grid, Grid[] gridHistory) {
        final var controller = new GameController(opponent, grid);
        controller.setPoint(point);

        if (controller.getState() == GameState.running) {
            final var newGrid = controller.getGrid();
            final var newGridHistory = addOneToGridHistory(gridHistory, newGrid);
            callOtherMoves(newGrid, newGridHistory);
        } else {
            final var lastHistory = addOneToGridHistory(gridHistory, controller.getGrid());
            assertNotEquals(
                    controller.getState(),
                    GameState.won,
                    "\nThis scenario has been lost: \n" + gridHistoryToString(lastHistory)
            );
        }
    }

    private void callOtherMoves(Grid grid, Grid[] gridHistory) {
        for (var point : grid.getAllMarkPositions(null)) {
            makeOneMove(point, grid, gridHistory);
        }
    }

    private Grid[] addOneToGridHistory(Grid[] history, Grid next) {
        final var newHistory = new Grid[history.length + 1];
        System.arraycopy(history, 0, newHistory, 0, history.length);
        newHistory[newHistory.length - 1] = next;
        return newHistory;
    }

    private String gridHistoryToString(Grid[] history) {
        StringBuilder s = new StringBuilder();
        for (var grid : history) {
            s.append(grid.toString("  "));
            s.append("\n");
        }
        return s.toString();
    }
}