package opponent.default_opponents;

import controller.GameState;
import domain.Grid;
import domain.Mark;
import domain.Point;
import opponent.Opponent;
import opponent.helper_classes.Node;
import opponent.helper_classes.Tree;

public class GodOpponent extends Opponent {

    @Override
    public Point move(Grid grid) {
        Tree<Node> tree = new Tree<>(new Node(grid, Mark.self, GameState.running), 0);
        generate(tree, Mark.opponent);
        sortTree();
        System.out.println(tree);
        Node bestMove = minimax(tree, 0, Mark.opponent, Integer.MIN_VALUE, Integer.MAX_VALUE); //TODO: put in for-loop and call with root-childs
        //int bestScore = minimax(tree, 9, Mark.opponent, Integer.MIN_VALUE, Integer.MAX_VALUE);
        //System.out.println(bestScore);
        //return null;
        System.out.println("Aim Child from Root" + "\n" + bestMove.toString(""));
        return compareGrids(tree.getRoot().grid(), bestMove.grid());
    }

    private void generate(Tree<Node> root, Mark actor) {
        final Grid rootGrid = root.getRoot().grid();
        for (Point point : rootGrid.getAllMarkPositions(null)) {
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
        //tree erst ab "rootGrid.getAllMarkPositions(null).size()==5" betrachten,
        //da erst dann ein spiel beendet sein kann

        //wenn ein Folgezug ein "GameState==won" liefert erfolgt ein `cutoff`,
        //unter dem punkt, wo ein subtree weiter oben im pfad,
        //eine subnode mit einem positiven score vorweisen kann

        //-depth-first search
        //wenn leaf gefunden wird, wird nach dem GameState entschieden:
        //bei einem win wird nach oben gegangen,
        //dabei wird jedes mal an einer node mit dem actor opponent geprüft,
        //ob ein sich ein einem subtree ein leaf mit lost, auf einer höheren ebende befindet
        //solange dies nicht der fall ist wird nicht abgebrochen
    }

    private Node minimax(Tree<Node> tree, int depth, Mark actor, int alpha, int beta) {
        System.out.println("Run minimax("+tree.getRoot().toString()+","+depth+","+actor+","+alpha+","+beta+")");
        if(tree.getRoot().leaf()) {
            System.out.println("return: (case1)");
            System.out.println(tree.getRoot().toString());
            return tree.getRoot();
        }
        Node bestVal = new Node(tree.getRoot().grid(), tree.getRoot().mark(), tree.getRoot().gameState());
        if(actor == Mark.opponent) {
            bestVal.setScore(Integer.MIN_VALUE);
            for(Tree<Node> t : tree.getSubTrees()) {
                System.out.println("call minimax(max) (" +
                        t.getRoot().toString() + "," +
                        (depth+1) + "," +
                        Mark.self + "," +
                        alpha + "," +
                        beta +")"
                );
                Node value = minimax(t, depth+1, Mark.self, alpha, beta);
                if(bestVal.score() < value.score()) {
                    bestVal = value;
                    //bestVal.setScore(value.score());
                }
                alpha = Integer.max(alpha, bestVal.score());
                if(beta <= alpha) {
                    break;
                }
            }
            System.out.println("return: (case2)");
            System.out.println(bestVal);
        } else {
            bestVal.setScore(Integer.MAX_VALUE);
            for(Tree<Node> t : tree.getSubTrees()) {
                System.out.println("call minimax(min) (" +
                        t.getRoot().toString() + "," +
                        (depth+1) + "," +
                        Mark.opponent + "," +
                        alpha + "," +
                        beta +")"
                );
                Node value = minimax(t, depth + 1, Mark.opponent, alpha, beta);
                if(bestVal.score() > value.score()) {
                    //bestVal.setScore(value.score());
                    bestVal = value;
                }
                beta = Integer.min(beta, bestVal.score());
                if (beta <= alpha) {
                    break;
                }
            }
            System.out.println("return: (case3)");
            System.out.println(bestVal);
        }
        return bestVal;
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
