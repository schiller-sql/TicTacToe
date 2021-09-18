package opponent;

import domain.*;

import java.util.*;
import java.util.stream.IntStream;

public class MinimaxOpponent extends AdvancedBaseOpponent {
    ;

    @Override
    public Point move(Grid grid) {
        final Tree<Node> tree = new Tree<>(new Node(grid));
        generateTreeForRoot(tree, Mark.opponent);//generates the tree based on the given grid
        List<Node> lastChildren = new ArrayList<>(getLastChildren(tree, new ArrayList<>())); //find all lastChildren in the root tree
        //calculates for every lastChildren the score
        IntStream.range(0, lastChildren.size())
                .forEachOrdered(i -> lastChildren.get(i)
                        .addScore(calculateScore(lastChildren.get(i))));

        //TODO: do this as long as all root leafes have exactly one best score
        //give the score from each child to their parents
        //by choosing the best score
        giveScoreToParent(tree, lastChildren);

       /* //check for each node in the tree if there are more than one score inherit by children and if yes, eliminate all others saved scores in this node
        setBestScore(tree);*/


        //so the best score is returns for the earliest game stat the opponent can win, because by time there are less open fields
        //if tie(): position null, int schore
        /*save best new score
        basend on who the max_player is
        this effects that the min() returns the most negativ score
        and the max() the highest score
         */

        return new Point(0, 0);
    }

    //done
    public void generateTreeForRoot(Tree<Node> root, Mark player) {
        final Grid rootGrid = root.getHead().getGrid();
        Point[] markTypesNull = rootGrid.getAllOfMarkType(null);

        for (Point point : markTypesNull) { //at start should be 8
            final Grid childGrid = rootGrid.copyWith(point, player);
            final Tree<Node> childTree = root.addLeaf(new Node(childGrid, player));
            generateTreeForRoot(childTree, Mark.invert(player));
        }
    }

    //done
    public List<Node> getLastChildren(Tree<Node> root, List<Node> nodes) {
        for(Tree<Node> child : root.getSubTrees()) {
            if(child.getSubTrees().size()==0) {
                nodes.add(child.getHead());
            }
            getLastChildren(child, nodes);
        }
        return nodes;
    }

    /*
    public List<Node> setBestScore(Tree<Node> root) {
        for(Tree<Node> child : root.getSubTrees()) {
            if(child.getHead().getScores().size()>1) {
                //Für fertigstellung: bestScore für child ermitteln und alle anderen löschen
            }
            setBestScore(child);
        }
        return null;
    } */

    //done
    private void giveScoreToParent(Tree<Node> tree, List<Node> lastChildren) {
        IntStream.range(0, lastChildren.size()) //gebe den best score von jedem children an den parent weiter
                .forEach(i -> lastChildren.get(i)
                        .getParent().addScore(bestScore(lastChildren.get(i))));
        /* for(int i = 0; i < lastChildren.size(); i++) {
            lastChildren.get(i).addScore(calculateScore(lastChildren.get(i)));
        } */
    }

    //not done because calculateEmptyFields() have to be finished first
    private int calculateScore(Node node) {
        Mark minimaxStatus = node.getMinimax();
        int score = minimaxStatus==Mark.opponent ? 1 : -1*(calculateEmptyFields(node)+1);
        return score;
    }

    //not done
    private int calculateEmptyFields(Node node) {
        //TODO:
        return 0;
    }

    //not done
    private int bestScore(Node node) {
        //TODO:
        return 0;
    }

    private void minimax(Point position, Grid grid, int score) {
        /*
        Berechne für jedes Spielende den Score, dann gebe alle Scores nach oben weiter
        bei mimimize wird der schlechteste Score weitergegeben
        bei maximize der beste
         */

        /*
        1. Suche alle Grid die den Spielstatus beendet haben(gewonnen, verloren, unentschieden),
            also alle Child Elemente, die keine leafs haben

            List<Node> lastChilds;
            for each calculateScore();

        2. Schaue ob Node.getMinimax() false oder true gibt (minimize oder maximize)
           dann gebe den jeweiligen 'bestenScore' an den Parent weiter mit Node.setMinimax()
           */
    }

}