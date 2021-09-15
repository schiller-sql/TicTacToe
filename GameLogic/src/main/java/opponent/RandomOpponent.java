package opponent;

import domain.Grid;
import domain.Point;

public class RandomOpponent extends AdvancedBaseOpponent {

    @Override
    public Point move(Grid grid) {
        return grid.getAllOfMarkType(null)[(int) (Math.random() * grid.getAllOfMarkType(null).length)];
    }
}