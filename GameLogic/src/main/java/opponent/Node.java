package opponent;

import domain.Grid;

public class Node implements Tree.TreePrintable {

    private Grid grid;
    private int score;
    private boolean minimax;

    public Node(Grid grid, int score, boolean minimax) {
        this.grid = grid;
        this.score = score;
        this.minimax = minimax;
    }

    public Node(Grid grid) {
        this.grid = grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setMinimaxStatus(boolean minimax) {
        this.minimax = minimax;
    }

    public Grid getGrid() {
        return grid;
    }

    public int getScore() {
        return score;
    }

    public boolean getMinimax() {
        return minimax;
    }

    @Override
    public String toString(String padding) {
        return padding + "Score: " + score
                + "\n" + grid.toString(padding);
    }
}
