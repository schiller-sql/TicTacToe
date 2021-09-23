package opponent.default_opponents;

import opponent.Opponent;
import opponent.OpponentTest;
import org.junit.jupiter.api.Disabled;

@Disabled
class AlgorithmOpponentTest extends OpponentTest {

    @Override
    protected Opponent getOpponent() {
        return new AlgorithmOpponent();
    }
}