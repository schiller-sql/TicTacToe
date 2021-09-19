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
        Node aim = null;
        final Tree<Node> tree = new Tree<>(new Node(grid)); //add the given grid as tree root
        generateTreeForRoot(tree, Mark.opponent);//generates the tree based on the given grid
        List<Node> lastChildren = getLastChildren(tree); //find all lastChildren in the root tree
        //System.out.println("Last Children: " + lastChildren.size()); //Find 25873
        //lastChildren.stream().map(lastChild -> lastChild.getGrid().asString()).forEach(System.out::println);

        for (Node lastChild : lastChildren) { //calculates the score for each lastChildren
            int score = calculateScore(lastChild);
            //System.out.println("Score: " + score + ", " + lastChild.getGrid().asString() + ", " + lastChild.getGameState());
            lastChild.addScore(score);
        } //TODO: should be functionally until here

        addScoreToRootLeafs(tree, lastChildren); //push all scores to the root leafs //TODO: duplicate scores

        List<Tree<Node>> rootLeafs = new ArrayList<>(tree.getSubTrees()); //prints an error for each root leaf, that don't have a count of scores of one
        for (Tree<Node> rootLeaf : rootLeafs) {
            Node node = rootLeaf.getHead();
            if (node.getScores().size() != 1) {
                System.err.println("Node: " + node.getGrid().asString() + " has a scores of " + Arrays.toString(node.getScores().toArray()));
            }
        }


        for (Tree<Node> child : tree.getSubTrees()) {//make sure every root leaf have a final score //TODO: not tested yet
            if (child != null) {
                System.out.println("child: " + Arrays.toString(child.getHead().getScores().toArray())); //TODO: to much scores, where did they come from?
            }
        }


        final int bestScore=bestScore(tree.getHead());
        //TODO: set aim here as the grid stores the best score
        System.out.println("Best Score: " + bestScore + ", Next Grid: " + aim.getGrid().asString()); //TODO: aim is null

        return compareGrids(tree.getHead().getGrid(), aim.getGrid()); //return the point from the grid with the best score //TODO: error here => NullPointerException for more see the todo above
    }

    private void addScoreToRootLeafs(Tree<Node> root, List<Node> lastChildren) {
        List<Node> parents = new ArrayList<>(); //#NEWLINE
        //for each layer of the Tree
        for(int i = root.getDept(); i > 0; i--) {
            //for each given children
            for(Node child : lastChildren) {
                //with the current depth
                if(child.getDepth()==i) { //if depth < 2 => found no child
                    //if the parent of the child is not null (else NullPointer)
                    if(child.getParent(root.locate.get(child))!=null) {
                        //set the score from the current child to his parent =>
                        child.getParent(root.locate.get(child)) //find the parent
                                .getHead() //get the parent head
                                .addScore(bestScore(child)); //add bestScore from the child to the parent
                        parents.add(child.getParent(root.locate.get(child)).getHead()); //#NEWLINE
                    }
                }
            }
        }
        if(parents.size()>0) {//#NEWLINE
            addScoreToRootLeafs(root, parents); //#NEWLINE
        }//#NEWLINE
    }





















    private List<Node> getLastChildren(Tree<Node> root) { //return the false children
        //root.locate.forEach((key, value) -> System.out.println(key.getGrid().asString() + "  |  " + value.getHead().getGrid().asString()));
        // TODO: DEBUGGING WEG MACHEN!!!!!
        /*root.locate.forEach((key, value) -> System.out.println(key.getGameState() + ":" + key.getGrid().asString() +
                "  |  " + value.getHead().getGameState() + ":" + value.getHead().getGrid().asString())); */
        return new ArrayList<>(root.allTrees()
                .stream()
                .filter(t -> t.getHead().getGameState() != GameState.running)
                .map(Tree::getHead)
                .toList());
    }

    private void generateTreeForRoot(@NotNull Tree<Node> root, Mark player) {
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
                generateTreeForRoot(childTree, Mark.invert(player), depth+1); //do again with child
            }
        }
    }

    //calculates the score depends on how deep the node is saved in the tree
    private int calculateScore(@NotNull Node node) {
        final int i = calculateEmptyFields(node) + 1;
        if (node.getMinimax() == Mark.opponent) {
            return i;
        }
        return -i;
    }

    //calculates the empty fields
    private int calculateEmptyFields(@NotNull Node node) {
        return node.getGrid().getAllOfMarkType(null).length;
    }

    //choose the best score for maximize or minimize
    //so the best score is returns for the earliest game stat the opponent can win, because by time there are less open fields
    //if the MinimaxStatus is equal to Mark.opponent the method return the highest score otherwise the lowest
    private int bestScore(@NotNull Node node) {
        if (node.getMinimax() == Mark.opponent) {
            return Collections.max(node.getScores());
        }
        return Collections.min(node.getScores());
    }

    //compare two grids and returns the different as a Point, if there is no different the method will return null
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

    private boolean checkForDiagonals(Mark mark, @NotNull Grid grid) {
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