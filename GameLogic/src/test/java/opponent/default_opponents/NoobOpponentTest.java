package opponent.default_opponents;

import domain.Point;
import opponent.Opponent;
import opponent.OpponentTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.GridUtils.getGridFromString;

class NoobOpponentTest extends OpponentTest {

    @Override
    protected Opponent getOpponent() {
        return new NoobOpponent();
    }

    @Test
    void move1() { //generates a simple sized Tree
        final var grid = getGridFromString( //check
                """
                        ∙ | ∙ | ∙
                        ∙ | X | ∙
                        ∙ | ∙ | ∙
                        """
        ); //Output should be: 2,2

        final var result = opponent.move(grid);

        assertEquals(new Point(2, 2), result);
    }
}