package opponent.default_opponents;

import controller.GameController;
import controller.GameState;
import domain.Grid;
import domain.Mark;
import domain.Point;
import opponent.Opponent;
import opponent.helper_classes.Node;
import opponent.helper_classes.Tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class GodOpponent extends Opponent {

    /*
    function minimax(node, depth, isMaximizingPlayer, alpha, beta):
    if node is a leaf node :
        return value of the node
    if isMaximizingPlayer :
        bestVal = -INFINITY
        for each child node :
            value = minimax(node, depth+1, false, alpha, beta)
            bestVal = max( bestVal, value)
            alpha = max( alpha, bestVal)
            if beta <= alpha:
                break
        return bestVal
    else :
        bestVal = +INFINITY
        for each child node :
            value = minimax(node, depth+1, true, alpha, beta)
            bestVal = min( bestVal, value)
            beta = min( beta, bestVal)
            if beta <= alpha:
                break
        return bestVal
// Calling the function for the first time.
minimax(0, 0, true, -INFINITY, +INFINITY)
     */

    @Override
    public Point move(Grid grid) {
        Tree<Node> tree = new Tree<>(new Node(grid, Mark.self, GameState.running), 0);
        generate(tree, Mark.opponent);
        Node bestMove = minimax(tree, 0, Mark.opponent, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return compareGrids(tree.getRoot().grid(), bestMove.grid());
    }

    private void generate(Tree<Node> root, Mark actor) {
        final Grid rootGrid = root.getRoot().grid();
        for (Point point : rootGrid.getAllOfMarkType(null)) {
            final Node node = new Node(
                    rootGrid.copyWith(point, actor),
                    actor,
                    calculateGameState(rootGrid.copyWith(point, actor))
            );
            Tree<Node> childTree = root.addLeaf(node);
            if (node.gameState() == GameState.running) {
                generate(childTree, Mark.invert(actor));
            } else {
                node.calculateScore();
                node.setLastChild(true);
            }
        }
    }

    private void sortTree() {

    }

    private void evaluateScores() {

    }

    private Node minimax(Tree<Node> tree, int depth, Mark actor, int alpha, int beta) {
        if(tree.getRoot().lastChild()) {
            return tree.getRoot();
        }
        int bestScore;
        Node bestMove = null;
        if(actor == Mark.opponent) {
            bestScore = Integer.MIN_VALUE;
            Collection<Tree<Node>> c = tree.getSubTrees();
            for(Tree<Node> t : c) {
                bestMove = minimax(t, depth+1, Mark.self, alpha, beta);
            }
            bestScore = Integer.max(bestScore, bestMove.score());
            alpha = Integer.max(alpha, bestScore);
            if(beta <= alpha) {
                bestMove.setScore(bestScore);
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            Collection<Tree<Node>> c = tree.getSubTrees();
            for(Tree<Node> t : c) {
                bestMove = minimax(t, depth+1, Mark.opponent, alpha, beta);
            }
            bestScore = Integer.min(bestScore, bestMove.score());
            beta = Integer.min(beta, bestScore);
            if(beta <= alpha) {
                bestMove.setScore(bestScore);
                return bestMove;
            }
        }
        return bestMove;
    }

    public Point compareGrids(Grid root, Grid leaf) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (leaf.getMark(i, j) != root.getMark(i, j)) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }

    private GameState calculateGameState(Grid grid) {
        if (checkWonOrLost(Mark.self, grid)) {
            return GameState.won;
        } else if (checkWonOrLost(Mark.opponent, grid)) {
            return GameState.lost;
        } else if (checkTie(grid)) {
            return GameState.tie;
        }
        return GameState.running;
    }

    private boolean checkTie(Grid grid) {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (grid.markIsEmpty(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkWonOrLost(Mark mark, Grid grid) {
        return checkForDiagonals(mark, grid) || checkForLines(mark, grid);
    }

    private boolean checkForDiagonals(Mark mark, Grid grid) {
        final boolean middleIs = grid.getMark(1, 1) == mark;
        final boolean firstDiagonal = grid.getMark(0, 0) == mark && grid.getMark(2, 2) == mark;
        final boolean secondDiagonal = grid.getMark(0, 2) == mark && grid.getMark(2, 0) == mark;
        return middleIs && (firstDiagonal || secondDiagonal);
    }

    private boolean checkForLines(Mark mark, Grid grid) {
        return checkForLine(mark, true, grid) || checkForLine(mark, false, grid);
    }

    private boolean checkForLine(Mark mark, boolean vertical, Grid grid) {
        xLoop:
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (grid.getMark(vertical ? x : y, vertical ? y : x) != mark) {
                    continue xLoop;
                }
            }
            return true;
        }
        return false;
    }
}
