package opponent;

import domain.Grid;
import domain.Point;
import opponent.default_opponents.*;

/**
 * Represents an Opponent to the player in tic-tac-toe,
 * which has to answer to the player
 */
public abstract class Opponent {

    /**
     * Get all currently working Opponents
     *
     * @return All Opponents as an Array
     */
    public static Opponent[] defaultOpponents() {
        return new Opponent[]{
                new ExampleOpponent(),
                new MinimaxOpponent(),
                new OleOpponent(),
                new QuandaryOpponent(),
                new RandomOpponent(),
                new TonyRandomOpponent(),
        };
    }

    /**
     * @return The name of the Opponent,
     * by default is the class's simple name, formatted nicely
     * @implNote Should only be implemented,
     * if there is some parameter to be displayed, e.g. difficulty
     */
    public String getName() {
        final String simpleName = this.getClass().getSimpleName();
        final StringBuilder newNameBuilder = new StringBuilder();
        for (int i = 0; i < simpleName.length(); i++) {
            char c = simpleName.charAt(i);
            if (i != 0 && Character.isUpperCase(c)) {
                newNameBuilder.append(" ").append(Character.toLowerCase(c));
            } else {
                newNameBuilder.append(c);
            }
        }
        return newNameBuilder.toString();
    }

    /**
     * An Opponent should implement this method to
     * answer to the player, when the player sets a mark
     *
     * @param grid The current state of the tic-tac-toe field
     * @return The point on where the opponent will place its mark
     */
    public abstract Point move(Grid grid);
}