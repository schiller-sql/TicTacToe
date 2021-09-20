package opponent;

import controller.GameState;
import domain.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.IntStream;

public class MinimaxOpponent extends AdvancedBaseOpponent {
     /*
    Total number of possible combinations is 3^9 = 19683.
    There are 5477 possible legal game states.
     */


    @Override
    public Point move(Grid grid) {
        /*
         Define the aim Node temporary as a valid Point
         This prevents NullPointerExceptions
         */
        Node aim = new Node(grid.copyWith(grid.getAllOfMarkType(null)[0], Mark.opponent));

        /*
         Set the given grid as tree root
         */
        final Tree<Node> tree = new Tree<>(new Node(grid));

        /*
         Generates the tree, based on the given grid
         The result will be a tree, who contains all possible future games
         */
        generateTreeForRoot(tree, Mark.opponent, 1);

        for (Node n : tree.locate.keySet()) {
            System.out.println(n.getDepth());
        }
        /*
         prints the tree
         */
        //System.out.println(tree);

        /*
         Finds all lastChildren in the root tree
         */
        List<Node> lastChildren = getLastChildren(tree);

        /*
         prints the count of last children
         and all last children as grid
         */
        //System.out.println("Last Children: " + lastChildren.size()); //Find 25873
        //lastChildren.stream().map(lastChild -> lastChild.getGrid().asString()).forEach(System.out::println);

        /*
        Calculates the score for each lastChildren
        */
        for (Node lastChild : lastChildren) {
            int score = calculateScore(lastChild);
            //System.out.println("Score: " + score + ", " + lastChild.getGrid().asString() + ", " + lastChild.getGameState());
            lastChild.addScore(score);
        }

        /**
         * Push all scores to the root leafs
         * TODO: this method may contains logic problems
         */
        addScoreToRootLeafs(tree);

        /*
         Prints an error for each root leaf, that don't have a count of scores of one (should be exactly one score in each child of root)
         */
        List<Tree<Node>> rootLeafs = new ArrayList<>(tree.getSubTrees());
        for (Tree<Node> rootLeaf : rootLeafs) {
            Node node = rootLeaf.getHead();
            if (node.getScores().size() != 1) {
                System.err.println("Node: " + node.getGrid().asString() + " has a scores of " + Arrays.toString(node.getScores().toArray()));
            }
        }
        /*
         * Print for each child all scores
         TODO: print to much scores
         The problem maybe comes from the  addScoreToRootLeafs(tree, lastChildren) method?!
         */
        for (Tree<Node> child : tree.getSubTrees()) {
            if (child != null) {
                System.out.println("child: " + Arrays.toString(child.getHead().getScores().toArray()));
            }
        }

        final int bestScore = 0; //TODO
        //TODO: set aim here as the grid, which stores the best score!!!
        System.out.println("Best Score: " + bestScore + ", Next Grid: " + aim.getGrid().asString()); //TODO: aim is null, see todo above

        return compareGrids(tree.getHead().getGrid(), aim.getGrid()); //return the point from the grid with the best score //TODO: error here => NullPointerException for more see the todo above
    }

    /**
     * TODO: warning => this method may contain unknown logical problems
     * This method pushes the scores from all last children ordered to the root leafs of the root tree
     *
     * @param root the root tree
     *             // * @param lastChildren all children that get a new score in the last run, at the beginning this param is the list of lastChildren
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

    private void giveToParent(Tree<Node> t) {
        final int score = bestScore(t.getHead());
        final Tree<Node> parent = t.getParent();
        parent.getHead().addScore(score);
    }


//below here all method are hopefully functional

    // FALSCH: // Nicht mehr nötig, da die children über ihre depth gefunden werden, bzw. locate

    /**
     * Search all children in the tree, with a different game state than 'running'
     *
     * @param root the tree to search in
     * @return a List of all the last children in root
     */
    private List<Node> getLastChildren(Tree<Node> root) {
        // TODO: DEBUGGING WEG MACHEN!!!!!
        /*root.locate.forEach((key, value) -> System.out.println(key.getGameState() + ":" + key.getGrid().asString() +
                "  |  " + value.getHead().getGameState() + ":" + value.getHead().getGrid().asString())); */
        return new ArrayList<>(root.allTrees()
                .stream()
                .filter(t -> t.getHead().getGameState() != GameState.running)
                .map(Tree::getHead)
                .toList());
    }

    /**
     * Generates the tree
     *
     * @param root   the root tree
     * @param player the actor of the current layer in the tree (self/opponent)
     * @param depth  the current depth of the tree
     */
    private void generateTreeForRoot(@NotNull Tree<Node> root, Mark player, int depth) {
        final Grid rootGrid = root.getHead().getGrid(); //gets the root grid
        Point[] markTypesNull = rootGrid.getAllOfMarkType(null); //get all empty points

        for (Point point : markTypesNull) {
            final Node node = new Node(
                    rootGrid.copyWith(point, player),
                    player,
                    calculateGameState(rootGrid.copyWith(point, player)),
                    depth
            );
            Tree<Node> childTree = root.addLeaf(node); //create new children from root with new generated head
            if (calculateGameState(node.getGrid()) == GameState.running) {
                generateTreeForRoot(childTree, Mark.invert(player), depth + 1); //do again with child
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
        if (node.getMinimax() == Mark.opponent) {
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