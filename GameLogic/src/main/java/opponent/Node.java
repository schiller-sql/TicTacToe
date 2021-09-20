package opponent;

import controller.GameState;
import domain.Grid;
import domain.Mark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Node implements Tree.TreePrintable {

    private Grid grid;
    private List<Integer> score = new ArrayList<>();
    private Mark minimax;
    private GameState gameState;
    private int depth;

    public Node(Grid grid, int score, Mark minimax) {
        this.grid = grid;
        this.score.add(score);
        this.minimax = minimax;
    }

    public Node(Grid grid, Mark minimax, GameState gameState , int depth) {
        this.grid=grid;
        this.minimax=minimax;
        this.gameState=gameState;
        this.depth=depth;
    }

    public Node(Grid grid) {
        this.grid = grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public void addScore(int score) {
        this.score.add(score);
    }

    public void setMinimaxStatus(Mark minimax) {
        this.minimax = minimax;
    }

    public Grid getGrid() {
        return grid;
    }

    public List<Integer> getScores() {
        return score;
    }

    public Mark getMinimax() {
        return minimax;
    }

    public GameState getGameState() {
        return gameState;
    }

    public int getDepth() {
        return depth;
    }

    @Override
    public String toString(String padding) {
        /*
        return padding + "Scores: " + Arrays.toString(score.toArray())
                + "\n" + padding +"MinimaxStatus: " + minimax
                + "\n" + grid.toString(padding); */
        return "abc";
    }

    public String asString() {
        return "Scores: " + Arrays.toString(score.toArray()) + " + "
                +"MinimaxStatus: " + minimax + " + "
                +"GameState: " + gameState + " + "
                +"Depth: " +  getDepth() + " + "
                +"Grid: " +  grid.asString();
    }

    public Tree<Node> getParent(Tree<Node> tree) {
        return tree.getParent();
    }
}
