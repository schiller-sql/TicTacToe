package opponent.default_opponents;

import domain.Point;
import opponent.Opponent;
import opponent.OpponentTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.GridUtils.getGridFromString;

class MinimaxOpponentTest extends OpponentTest {

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
        return new MinimaxOpponent();
    }
}