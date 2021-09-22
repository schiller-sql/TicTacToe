package opponent;

import controller.GameState;
import domain.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MinimaxOpponent extends AdvancedBaseOpponent {
     /*
    Total number of possible combinations is 3^9 = 19683.
    There are 5477 possible legal game states.
     */


    @Override
    public Point move(Grid grid) {

        // Define the aim Node temporary as a valid Point, this prevents NullPointerExceptions
        Node aim = new Node(grid.copyWith(grid.getAllOfMarkType(null)[0], Mark.opponent));
        final Tree<Node> tree = new Tree<>(new Node(grid));


        //Generates the tree, based on the given grid, the result will be a tree, who contains all possible future games
        generateTreeForRoot(tree, Mark.opponent, 0);
        System.out.println();
        System.out.println(tree);
        //Push all scores to the root leafs
        System.out.println();
        System.out.println("Push all scores to the root leafs...");
        System.out.println();
        addScoreToRootLeafs(tree);
        System.out.println(tree);

        int bestScore = -5;

        for (Tree<Node> child : tree.getSubTrees()) {
            if (child != null) {
                child.getHead().setMinimaxStatus(Mark.invert(child.getHead().getMinimax())); //invert the minimaxStatus of the child
                child.getHead().setScore(bestScore(child.getHead()));
            } else {
                System.err.println("ERROR");
            }
            System.out.println("Root child: " + child.getHead().getGrid().asString() + ", final score: " + Arrays.toString(child.getHead().getScores().toArray()));
        }

        for (Tree<Node> child : tree.getSubTrees()) {
            if (child != null) {
                if(bestScore < bestScore(child.getHead())) {
                    bestScore = Collections.min(child.getHead().getScores());
                    System.out.println("new best score: " + bestScore);
                }
            }
        }

        //find the grid to the score
        List<Tree<Node>> rootLeafs = new ArrayList<>(tree.getSubTrees());
        for (Tree<Node> rootLeaf : rootLeafs) { //for each root leaf
            Node node = rootLeaf.getHead();
            for(int i = 0; i < node.getScores().size(); i++) { //for each score in the current leaf head
                if(node.getScores().get(i)==bestScore) {
                    aim = node;
                   /* System.out.println("The grid " + node.getGrid().asString() + " with the score of " + node.getScores().get(i)
                            + " has the best Score: " + bestScore); */
                }
            }
        }
        System.out.println("ROOT SCORES: " + tree.getHead().getScores());
        System.out.println("Best Score: " + bestScore + ", Next Grid: " + aim.getGrid().asString()); //TODO: aim is null, see todo above
        System.out.println(compareGrids(tree.getHead().getGrid(), aim.getGrid()).x() + ":" + compareGrids(tree.getHead().getGrid(), aim.getGrid()).y());

        return compareGrids(tree.getHead().getGrid(), aim.getGrid()); //return the point from the grid with the best score //TODO: error here => NullPointerException for more see the todo above
    }

    /**
     * This method pushes the scores from all last children ordered to the head of the root tree
     *
     * @param root the root tree
     */
    private void addScoreToRootLeafs(@NotNull Tree<Node> root) {
        final Collection<Tree<Node>> allTrees = root.locate.values();
        //for each layer of the Tree
        for (int i = 9; i > 0; i--) {
            final var a = i;
            allTrees
                    .stream()
                    .filter(t -> t.getHead().getDepth() == a)
                    .forEach(this::giveToParent);
        }
    }

    private void giveToParent(@NotNull Tree<Node> t) {
        final int score = bestScore(t.getHead());
        final Tree<Node> parent = t.getParent();
        parent.getHead().addScore(score);
    }

    private void generateTreeForRoot(@NotNull Tree<Node> root, Mark player, int depth) {
        final Grid rootGrid = root.getHead().getGrid(); //gets the root grid
        System.out.println("Generate tree below: " + rootGrid.asString());
        Point[] markTypesNull = rootGrid.getAllOfMarkType(null); //get all empty points

        for (Point point : markTypesNull) {
            final Node node = new Node(
                    rootGrid.copyWith(point, player),
                    player,
                    calculateGameState(rootGrid.copyWith(point, player)),
                    depth
            );
            System.out.println("Add node " + node.getGrid().asString() + " as leaf to the tree");
            Tree<Node> childTree = root.addLeaf(node); //create new children from root with new generated head
            if (calculateGameState(node.getGrid()) == GameState.running) { //if child is not a last children
                generateTreeForRoot(childTree, Mark.invert(player), depth + 1); //do again with child
            } else {
                final int score = calculateScore(node);
                node.addScore(score);
                if(player==Mark.opponent) {
                    System.out.println("Add score " + score + " to " + node.getGrid().asString() + " status=" + calculateGameState(node.getGrid()));
                    if(score<0 && (calculateGameState(node.getGrid())==GameState.lost || calculateGameState(node.getGrid())==GameState.tie)) { // 0 or below and lost or tie
                        System.err.println("Calculated false score");
                    }
                }
            }
        }
    }


    /**
     * Calculates the score depends on how deep the node is saved in the tree
     * If node.getMinimax() returns Mark.opponent, the score is positiv otherwise negativ
     *
     * @param node the node to calculate for
     * @return the score
     */
    private int calculateScore(@NotNull Node node) {
        final int i = calculateEmptyFields(node) + 1;
        if (node.getMinimax() == Mark.opponent) {
            return i;
        }
        return -i;
    }

    /**
     * Calculates the empty fields
     *
     * @param node the node to calculate for
     * @return the count of empty fields from the grid of the given node
     */
    private int calculateEmptyFields(@NotNull Node node) {
        return node.getGrid().getAllOfMarkType(null).length;
    }

    /**
     * Choose the best score for maximize or minimize
     * The best score is returns for the earliest game stat the opponent can win, because by time there are less open fields
     *
     * @param node the node to calculate the best score for
     * @return the best score of the given node, if the MinimaxStatus is equal to Mark.opponent the method return the highest score otherwise the lowest
     */
    private int bestScore(@NotNull Node node) {
        if (node.getMinimax() == Mark.opponent) { //TODO: i dont know if i have to check for Mark.opponent or Mark.self
            return Collections.max(node.getScores());
        }
        return Collections.min(node.getScores());
    }

    /**
     * Compare two grids and returns the different
     *
     * @param root
     * @param leaf
     * @return a Point of the different, if there is no different the method will return null
     */
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

    //TODO: duplicated code below

    /**
     * Calculates the game state
     *
     * @param grid the grid to test of
     * @return the game state of the grid
     */
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

    /**
     * Check if the grid is full of marks
     *
     * @param grid the grid to test of
     * @return true if the given grid is full
     */
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

    /**
     * Check if the game is won or lost
     *
     * @param mark the mark to test for
     * @param grid the grid to test of
     * @return true if the game is won or lost else false
     */
    private boolean checkWonOrLost(Mark mark, Grid grid) {
        return checkForDiagonals(mark, grid) || checkForLines(mark, grid);
    }

    /**
     * Check for all diagonal lines
     *
     * @param mark the mark to test for
     * @param grid the grid to test of
     * @return true if at least one diagonal line contains three marks of the same type else false
     */
    private boolean checkForDiagonals(Mark mark, @NotNull Grid grid) {
        final boolean middleIs = grid.getMark(1, 1) == mark;
        final boolean firstDiagonal = grid.getMark(0, 0) == mark && grid.getMark(2, 2) == mark;
        final boolean secondDiagonal = grid.getMark(0, 2) == mark && grid.getMark(2, 0) == mark;
        return middleIs && (firstDiagonal || secondDiagonal);
    }

    /**
     * Check for all vertical and all horizontal lines
     *
     * @param mark the mark to test for
     * @param grid the grid to test of
     * @return true if at least one horizontal or vertical line contains three marks of the same type else false
     */
    private boolean checkForLines(Mark mark, Grid grid) {
        return checkForLine(mark, true, grid) || checkForLine(mark, false, grid);
    }


    /**
     * Check for all vertical or all horizontal lines
     *
     * @param mark     the mark to test for
     * @param vertical if check on verticals or horizontals
     * @param grid     the grid to test of
     * @return true if at least one line contains three marks of the same type else false
     */
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