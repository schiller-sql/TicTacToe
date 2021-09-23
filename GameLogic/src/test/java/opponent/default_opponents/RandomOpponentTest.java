package opponent.default_opponents;

import opponent.Opponent;
import opponent.OpponentTest;

class RandomOpponentTest extends OpponentTest {

    @Override
    protected Opponent getOpponent() {
        return new RandomOpponent();
    }
}