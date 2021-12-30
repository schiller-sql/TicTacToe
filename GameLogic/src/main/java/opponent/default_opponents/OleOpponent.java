package opponent.default_opponents;

import domain.*;
import opponent.base_opponents.AdvancedBaseOpponent;

/**
 * Opponent places one point if it can win directly otherwise it tries to block the players rows.
 * If none of these cases is true opponent will place a random point.
 */
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