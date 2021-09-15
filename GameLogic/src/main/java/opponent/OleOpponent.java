package opponent;

import domain.*;

public class OleOpponent extends AdvancedBaseOpponent {

    @Override
    public Point move(Grid grid) {
        Point point;

        if (getRowFinalNeededPoints(grid, Mark.opponent).length == 0) {
            //wenn der Opponent selbst nicht gewinnen kann

            if (getRowFinalNeededPoints(grid, Mark.self).length != 0) {
                //dann prüfe, ob der Spieler gewinnen kann

                point = getRowFinalNeededPoints(grid, Mark.self)[0];
                //verhindere den Spielerzug

            } else {
                //wähle nächste freie stelle
                point = grid.getAllOfMarkType(null)[0];
            }
        } else {
            //wenn du als opponent gewinnen kannst
            point = getRowFinalNeededPoints(grid, Mark.opponent)[0];
        }
        return point;
    }
}