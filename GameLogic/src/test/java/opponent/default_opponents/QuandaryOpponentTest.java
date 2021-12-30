package opponent.default_opponents;

import opponent.Opponent;
import opponent.OpponentTest;

class QuandaryOpponentTest extends OpponentTest {

    @Override
    protected Opponent getOpponent() {
        return new QuandaryOpponent();
    }
}