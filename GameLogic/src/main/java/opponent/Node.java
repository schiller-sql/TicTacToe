package opponent;

import controller.GameState;
import domain.Grid;
import domain.Mark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is a wrapper class used to safe grids and values in the tree
 */
public class Node implements Tree.TreePrintable {

    private Grid grid;
    private final List<Integer> score = new ArrayList<>();
    private Mark minimax;
    private GameState gameState;
    private int depth;

    /**
     * @param grid the grid itself
     * @param minimax the actor of the grid
     * @param gameState the current game state of the grid
     * @param depth the depth of the node element in the tree
     */
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

    public void setScore(int score) {
        this.score.removeAll(this.score); //TODO: find a better solution
        //System.out.println(Arrays.toString(this.score.toArray()));
        this.score.add(score);
    }

    /**
     * @param padding the padding of the string
     * @return all data of the node in a formatted way
     */
    @Override
    public String toString(String padding) {

        return padding +"Depth: " + depth
                + "\n" + padding + "GameState: " + gameState + " => Scores: " + Arrays.toString(score.toArray())
                + "\n" + padding +"MinimaxStatus: " + minimax
                + "\n" + grid.toString(padding);
    }

    /**
     * @return all data of the node in one line
     */
    public String asString() {
        return "Scores: " + Arrays.toString(score.toArray()) + " + "
                +"MinimaxStatus: " + minimax + " + "
                +"GameState: " + gameState + " + "
                +"Depth: " +  getDepth() + " + "
                +"Grid: " +  grid.asString();
    }

    @SuppressWarnings("unused")
    public Tree<Node> getParent(Tree<Node> tree) {
        return tree.getParent();
    }
}
