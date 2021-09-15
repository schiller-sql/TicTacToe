package opponent;

import domain.*;

public class OleOpponent extends AdvancedBaseOpponent {

    @Override
    public Point move(Grid grid) {
        Point point;
        if (getRowFinalNeededPoints(grid, Mark.opponent).length == 0) {
            if (getRowFinalNeededPoints(grid, Mark.self).length != 0) {
                point = getRowFinalNeededPoints(grid, Mark.self)[0];
            } else {
                point = grid.getAllOfMarkType(null)[0];
            }
        } else {
            point = getRowFinalNeededPoints(grid, Mark.opponent)[0];
        }
        return point;
    }
}