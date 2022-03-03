package opponent.helper_classes;

import controller.GameState;
import domain.Grid;
import domain.Mark;
import opponent.default_opponents.GodOpponent;

public record Node(Grid grid, Mark mark, GameState gameState, int score) {

    private static boolean lastChild = false;

    public Node(Grid grid, Mark mark, GameState gameState) {
        this(grid, mark, gameState, calculateScore(grid, mark));
    }

        public void setLastChild(boolean isLastChild) {
        lastChild = isLastChild;
    }

    public boolean lastChild() {
        return lastChild;
    }

    private static int calculateScore(Grid grid, Mark mark) {
        final int i = grid.getAllOfMarkType(null).length;
        if (mark == Mark.opponent) {
            return i;
        }
        if(i == 0) {
            return i;
        } else {
            return -i;
        }
    }
}
