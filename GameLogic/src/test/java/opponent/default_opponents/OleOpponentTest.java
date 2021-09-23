package opponent.default_opponents;

import opponent.Opponent;
import opponent.OpponentTest;

class OleOpponentTest extends OpponentTest {
    @Override
    protected Opponent getOpponent() {
        return new OleOpponent();
    }
}