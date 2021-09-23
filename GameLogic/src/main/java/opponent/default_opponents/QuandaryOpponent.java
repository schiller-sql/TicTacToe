package opponent.default_opponents;

import domain.*;
import opponent.base_opponents.AdvancedBaseOpponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Opponent first checks if there is any possibility to win by placing one point
 * in second the opponent tries to stop the player from winning
 * otherwise opponent is checking if there is a possibility to create a quandary.
 * If none of these cases is true, opponent will place a random point.
 */
public class QuandaryOpponent extends AdvancedBaseOpponent {

    @Override
    public Point move(Grid grid) {

        if(getRowFinalNeededPoints(grid, Mark.opponent).length != 0) {
            return getRowFinalNeededPoints(grid, Mark.opponent)[0];
        } // checking if opponent can win by placing one point
        if (getRowFinalNeededPoints(grid, Mark.self).length != 0) {
            return getRowFinalNeededPoints(grid, Mark.self)[0];
        } // checking if player could win

        List<Grid> possibleNextMarks = new ArrayList<>();
        Point[] markTypesNull = grid.getAllOfMarkType(null);

        for (int i = 0; i < markTypesNull.length; i++) {
            possibleNextMarks.add(grid.copyWith(markTypesNull[i], Mark.opponent));
        } // creating an array-list with all possible next points
        for (int i = 0; i < possibleNextMarks.size(); i++)
        {
            if (getRowFinalNeededPoints(possibleNextMarks.get(i), Mark.opponent).length > 1) {
                return markTypesNull[i];
            } // checking if creating a quandary (possible points to win more than one) is possible
        }
        return markTypesNull[(int) (Math.random() * markTypesNull.length)];
    } // if no chance to win in next two rounds opponent chooses a random point
}