package opponent;

import domain.Grid;
import domain.Point;

/**
 * Represents an Opponent to the player in tic-tac-toe,
 * which has to answer to the player
 */
public interface Opponent {

    /**
     * An Opponent should implement this method to
     * answer to the player, when the player sets a mark
     *
     * @param grid The current state of the tic-tac-toe field
     * @return The point on where the opponent will place its mark
     */
    public Point move(Grid grid);
}