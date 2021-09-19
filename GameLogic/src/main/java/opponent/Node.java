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

    public Node(Grid grid, int score, Mark minimax) {
        this.grid = grid;
        this.score.add(score);
        this.minimax = minimax;
    }

    public Node(Grid grid, int score, Mark minimax, GameState gameState) {
        this.grid = grid;
        this.score.add(score);
        this.minimax = minimax;
        this.gameState=gameState;
    }

    public Node(Grid grid, int score) {
        this.grid=grid;
        this.score.add(score);
    }

    public Node(Grid grid, Mark minimax) {
        this.grid=grid;
        this.minimax=minimax;
    }

    public Node(Grid grid, Mark minimax, GameState gameState) {
        this.grid=grid;
        this.minimax=minimax;
        this.gameState=gameState;
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

    @Override
    public String toString(String padding) {
        /*
        return padding + "Scores: " + Arrays.toString(score.toArray())
                + "\n" + padding +"MinimaxStatus: " + minimax
                + "\n" + grid.toString(padding); */
        return "abc";
    }

    public String asString() {
        return "Scores: " + Arrays.toString(score.toArray()) + "; "
                +"MinimaxStatus: " + minimax + "; "
                +"Grid: " +  grid.asString();
    }

    public Tree<Node> getParent(Tree<Node> tree) {
        return tree.getParent();
    }
}
