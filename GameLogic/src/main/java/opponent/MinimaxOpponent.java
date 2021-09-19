package opponent;

import controller.GameState;
import domain.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public class MinimaxOpponent extends AdvancedBaseOpponent {
        /*
    Total number of possible combinations is 3^9 = 19683.
    There are 5477 possible legal game states.
     */


    @Override
    public Point move(Grid grid) {
        final int[] bestScore = {0}; //have to be an array to make it final usable -> have to be final to use it in lambda expression without waring
        AtomicReference<Grid> aimGrid = new AtomicReference<>();
        final Tree<Node> tree = new Tree<>(new Node(grid)); //add the given grid as tree root
        generateTreeForRoot(tree, Mark.opponent);//generates the tree based on the given grid
        List<Node> lastChildren = getLastChildren(tree); //find all lastChildren in the root tree
        System.out.println("Last Children: " + lastChildren.size()); //Find 25873
        /*for(int i = 0; i < lastChildren.size(); i++) {
            System.out.println(lastChildren.get(i).getGrid().asString());
        }*/

        for(int i = 0; i < lastChildren.size(); i++) {
            int score = calculateScore(lastChildren.get(i));
            System.out.println("Score: " + score + ", " + lastChildren.get(i).getGrid().asString() + ", " + lastChildren.get(i).getGameState());
            lastChildren.get(i).addScore(score);
        }

        addScoreToRootLeafs(tree, lastChildren); //push all scores to the root leafs //TODO: error here

        List<Tree<Node>> rootLeafs = new ArrayList<>(tree.getSubTrees());
        for (int i = 0; i < rootLeafs.size(); i++) {
            Node node = rootLeafs.get(i).getHead();
            if (node.getScores().size() == 0) {
                System.err.println("Node: " + node.getGrid().asString() + " has a score of " + Arrays.toString(node.getScores().toArray()));
            }
        }

        tree.getSubTrees().forEach(child -> {//make sure every root leaf have a final score //TODO: not tested yet
            System.out.println(Arrays.toString(child.getHead().getScores().toArray()));
            if (child.getHead().getScores().size() != 1) { //is 0
                System.err.println("Something went wrong");
                System.exit(-1);
            }
        });

        tree.getSubTrees().forEach(child -> {//choose the best score and the best grid //TODO: not tested yet
            if (child.getHead().getScores().get(0) > bestScore[0]) {
                bestScore[0] = child.getHead().getScores().get(0);
                aimGrid.set(child.getHead().getGrid());
            }
        });

        assert aimGrid != null;
        return compareGrids(tree.getHead().getGrid(), aimGrid.get()); //return the point from the grid with the best score //TODO: not tested yet
    }

    /*
    Soll eine List<Node> zurückgeben, die keine weiteren leafs besitzen
    (diese teilen die Eigenschaft, dass sie ein beendetes spiel repräsentieren)
     */

    private void addScoreToRootLeafs(Tree<Node> tree, List<Node> children) { //TODO: error here
        List<Node> parents = new ArrayList<>();
        for (Node child : children) {
            if (child.getParent(tree) != null) {
                child.getParent(tree).getHead()
                        .addScore(bestScore(child)); //add the best score from the children to the parent
                children.remove(child);
                parents.add(child.getParent(tree).getHead());     //what happend if I add and remov items from the list in loop?
            }
        }
        if (parents.size() > 0) {
            addScoreToRootLeafs(tree, parents);
        }
    }






    private List<Node> getLastChildren(Tree<Node> root) { //return the false children
        List<Node> lastChildren = new ArrayList<>();
        //root.locate.forEach((key, value) -> System.out.println(key.getGrid().asString() + "  |  " + value.getHead().getGrid().asString()));
        root.locate.forEach((key, value) -> System.out.println(key.getGameState() + ":" + key.getGrid().asString() +
                "  |  " + value.getHead().getGameState() + ":" + value.getHead().getGrid().asString()));
        lastChildren.addAll(root.locate
                .values()
                .stream()
                .filter(t -> t.getHead().getGameState()!=GameState.running)
                .map(Tree::getHead)
                .toList());
        return lastChildren;
    }

    private void generateTreeForRoot(@NotNull Tree<Node> root, Mark player) {
        final Grid rootGrid = root.getHead().getGrid(); //gets the root grid
        Point[] markTypesNull = rootGrid.getAllOfMarkType(null); //get all empty points

        for (Point point : markTypesNull) {
            //if(calculateGameState(rootGrid.copyWith(point, player))==GameState.running) { //TODO: erstellt nur laufende Grids
                Tree<Node> childTree = root.addLeaf(new Node(rootGrid.copyWith(point, player),
                        player,
                        calculateGameState(rootGrid.copyWith(point, player)))); //create new children from root with new generated head
                if(calculateGameState(childTree.getHead().getGrid())==GameState.running) {
                    generateTreeForRoot(childTree, Mark.invert(player)); //do again with child
                }
           // }
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