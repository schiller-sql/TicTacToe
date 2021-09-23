package opponent.default_opponents;

import opponent.Opponent;
import opponent.OpponentTest;

class TonyRandomOpponentTest extends OpponentTest {

    @Override
    protected Opponent getOpponent() {
        return new TonyRandomOpponent();
    }
}