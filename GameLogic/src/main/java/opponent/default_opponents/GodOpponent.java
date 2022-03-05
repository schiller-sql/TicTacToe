package opponent.default_opponents;

import controller.GameState;
import domain.Grid;
import domain.Mark;
import domain.Point;
import opponent.Opponent;
import opponent.helper_classes.Node;
import opponent.helper_classes.Tree;

import java.util.Collection;

public class GodOpponent extends Opponent {

    @Override
    public Point move(Grid grid) {
        Tree<Node> tree = new Tree<>(new Node(grid, Mark.self, GameState.running), 0);
        generate(tree, Mark.opponent);
        System.out.println(tree);
        Node bestMove = minimax(tree, 0, Mark.opponent, Integer.MIN_VALUE, Integer.MAX_VALUE); //TODO: put in for-loop and call with root-childs
        System.out.println("Aim Child from Root" + "\n" + bestMove.toString(""));
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
                node.setLeaf(true);
            }
        }
    }

    private void sortTree() {

    }

    //TODO: test for GodOpponentTest.move2, with println's
    /*
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
     */
    private Node minimax(Tree<Node> tree, int depth, Mark actor, int alpha, int beta) {
        System.out.println("Run minimax("+tree.getRoot().toString()+","+depth+","+actor+","+alpha+","+beta+")");
        if(tree.getRoot().leaf()) {
            System.out.println("return: (case1)");
            System.out.println(tree.getRoot().toString(""));
            return tree.getRoot();
        }
        Node bestMove = null;
        int bestVal;
        if(actor == Mark.opponent) {
            bestVal = Integer.MIN_VALUE;
            for(Tree<Node> t : tree.getSubTrees()) {
                Node value = minimax(t, depth+1, Mark.self, alpha, beta);
                bestVal = Integer.max(bestVal, value.score());
                alpha = Integer.max(alpha, bestVal);
                bestMove = value;
                bestMove.setScore(bestVal);
                if(beta <= alpha) {
                    break;
                }
            }
            System.out.println("return: (case2)");
        } else {
            bestVal = Integer.MAX_VALUE;
            for(Tree<Node> t : tree.getSubTrees()) {
                Node value = minimax(t, depth + 1, Mark.opponent, alpha, beta);
                bestVal = Integer.min(bestVal, value.score());
                beta = Integer.min(beta, bestVal);
                bestMove = value;
                bestMove.setScore(bestVal);
                if (beta <= alpha) {
                    break;
                }
            }
            System.out.println("return: (case3)");
        }
        System.out.println(bestMove.toString(""));
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
