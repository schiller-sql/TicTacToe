package fixtures;

import domain.Grid;
import domain.Mark;
import domain.Point;
import opponent.base_opponents.AdvancedBaseOpponent;

public class TestableAdvancedBaseOpponent extends AdvancedBaseOpponent {
    @Override
    public Point[] getRowFinalNeededPoints(Grid grid, Mark markType) {
        return super.getRowFinalNeededPoints(grid, markType);
    }

    @Override
    public Point move(Grid grid) {
        throw new Error("This class is only for testing purposes, it should not be tested in a GameController");
    }
}
