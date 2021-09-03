package fixtures;

import domain.Grid;
import domain.Point;
import opponent.Opponent;

public class FakeOpponent implements Opponent {

    final Point placingPoint;

    public FakeOpponent() {
        placingPoint = new Point(2,2);
    }

    public FakeOpponent(Point placingPoint) {
        this.placingPoint = placingPoint;
    }


    @Override
    public Point move(Grid grid) {
        return placingPoint;
    }
}