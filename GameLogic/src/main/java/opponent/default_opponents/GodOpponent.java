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

        return null;
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
                final int score = calculateScore(node);
                node.setLastChild(true);
            }
        }
    }

    private void sortTree() {

    }

    private void evaluateScores() {

    }

    private int minimax(Node node, int depth, Mark actor, int alpha, int beta) {
        if(node.lastChild()) {
            return node.score();
        }
        if(actor == Mark.opponent) {
            bestVal = -INFINITY
            for each child node :
            value = minimax(node, depth+1, false, alpha, beta)
            bestVal = max( bestVal, value)
            alpha = max( alpha, bestVal)
            if beta <= alpha:
            break
            return bestVal
        } else {
            bestVal = +INFINITY
            for each child node :
            value = minimax(node, depth+1, true, alpha, beta)
            bestVal = min( bestVal, value)
            beta = min( beta, bestVal)
            if beta <= alpha:
            break
            return bestVal
        }
    }

    public int calculateScore(Node node) {
        final int i = node.grid().getAllOfMarkType(null).length;
        if (node.mark() == Mark.opponent) {
            return i;
        }
        if(i == 0) {
            return i;
        } else {
            return -i;
        }
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

    private boolean getTie(Grid grid) {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (grid.markIsEmpty(x, y)) {
                    return false;
                }
            }
        }
        return true;
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
