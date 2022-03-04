package opponent.helper_classes;

import controller.GameState;
import domain.Grid;
import domain.Mark;
import opponent.default_opponents.GodOpponent;

public record Node(Grid grid, Mark mark, GameState gameState) {

    private static boolean lastChild = false;
    private static int score;

    public void setLastChild(boolean isLastChild) {
        lastChild = isLastChild;
    }

    public boolean lastChild() {
        return lastChild;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int score() {
        return score;
    }

    public int calculateScore() {
        final int i = this.grid.getAllOfMarkType(null).length;
        if (this.mark == Mark.opponent) {
            return i;
        }
        if(i == 0) {
            return i;
        } else {
            return -i;
        }
    }
}
