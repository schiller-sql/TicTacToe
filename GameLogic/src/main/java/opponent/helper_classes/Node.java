package opponent.helper_classes;

import controller.GameState;
import domain.Grid;
import domain.Mark;

import java.util.Objects;

public final class Node implements Tree.TreePrintable {

    private final Grid grid;
    private final Mark mark;
    private final GameState gameState;
    private boolean leaf;
    private int score;

    public Node(Grid grid, Mark mark, GameState gameState) {
        this.grid = grid;
        this.mark = mark;
        this.gameState = gameState;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int score() {
        return score;
    }

    public void calculateScore() {
        final int i = this.grid.getAllMarkPositions(null).size();
        if (mark.equals(Mark.opponent) || gameState.equals(GameState.lost)) {
            score = i;
        } else if(i == 0){
            score = -1;
        } else {
            score = -i;
        }
    }

    public String toString(String padding) {
        StringBuilder s = new StringBuilder();
        s.append(padding);
        s.append(grid.asString(padding));
        s.append(padding);
        s.append("mark = " + mark.toString()).append("\n");
        s.append(padding);
        s.append("GameState = " + gameState.toString()).append("\n");
        s.append(padding);
        s.append("score = " + score).append("\n");
        s.append(padding);
        s.append("isLeaf = " + leaf).append("\n");
        return s.toString();
    }

    public Grid grid() {
        return grid;
    }

    public Mark mark() {
        return mark;
    }

    public GameState gameState() {
        return gameState;
    }

    public boolean leaf() {
        return leaf;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Node) obj;
        return Objects.equals(this.grid, that.grid) &&
                Objects.equals(this.mark, that.mark) &&
                Objects.equals(this.gameState, that.gameState) &&
                this.leaf == that.leaf &&
                this.score == that.score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(grid, mark, gameState, leaf, score);
    }

    @Override
    public String toString() {
        return "Node[" +
                "grid=" + grid.toString() + ", " +
                "mark=" + mark + ", " +
                "gameState=" + gameState + ", " +
                "leaf=" + leaf + ", " +
                "score=" + score + ']';
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }
}
