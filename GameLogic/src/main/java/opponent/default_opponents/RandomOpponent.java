package opponent.default_opponents;

import domain.Grid;
import domain.Point;
import opponent.base_opponents.AdvancedBaseOpponent;

/**
 * Opponent chooses a random Point to return.
 */
public class RandomOpponent extends AdvancedBaseOpponent {

    @Override
    public Point move(Grid grid) {
        return grid.getAllOfMarkType(null)[(int) (Math.random() * grid.getAllOfMarkType(null).length)];
    }
}