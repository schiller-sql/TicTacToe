package opponent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static util.GridUtils.getGridFromString;

class MinimaxOpponentTest {

    MinimaxOpponent sut;

    @BeforeEach
    void init() {
        sut = new MinimaxOpponent();
    }

    @Test
    void move1() {
        final var grid = getGridFromString(
                """
                        O | O | O
                        O | O | O
                        O | O | O
                        """
        );

        sut.move(grid);
    }

    @Test
    void move2() {
        final var grid = getGridFromString(
                """
                        O | O | O
                        O | O | O
                        O | O | O
                        """
        );

        sut.move(grid);
    }

    @Test
    void move3() {
        final var grid = getGridFromString(
                """
                        O | O | O
                        O | O | O
                        O | O | O
                        """
        );

        sut.move(grid);
    }
}