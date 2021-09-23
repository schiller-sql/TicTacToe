package opponent;

import opponent.default_opponents.MinimaxOpponent;
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
    void move1() { //generates a simple sized Tree
        final var grid = getGridFromString( //check
                """
                        X | X | O
                        X | ∙ | O
                        ∙ | O | ∙
                        """
        ); //Output should be: 2,2

        sut.move(grid);
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

        sut.move(grid);
    }

    @Test
    void move3() { //generates a simple sized Tree
        final var grid = getGridFromString( //check
                """
                        O | X | X
                        O | O | X
                        ∙ | X | ∙
                        """
        ); //Output should be: 2,2 (0,2 have the same score)

        sut.move(grid);
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
        sut.move(grid);
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
            sut.move(grid);
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
        sut.move(grid);
    }
}