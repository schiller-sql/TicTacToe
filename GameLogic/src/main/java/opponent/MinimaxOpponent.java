package opponent;

import domain.*;

import java.util.*;
import java.util.stream.IntStream;

public class MinimaxOpponent extends AdvancedBaseOpponent {


    @Override
    public Point move(Grid grid) {
        final Tree<Node> tree = new Tree<>(new Node(grid)); //add the given grid as tree root
        generateTreeForRoot(tree, Mark.opponent);//generates the tree based on the given grid
        int dept = tree.getDept(tree);
        List<Node> lastChildren = new ArrayList<>(getLastChildren(tree, new ArrayList<>())); //find all lastChildren in the root tree
        //calculates for every lastChildren the score
        IntStream.range(0, lastChildren.size())
                .forEachOrdered(i -> lastChildren.get(i)
                        .addScore(calculateScore(lastChildren.get(i))));

        //do this as long as all root leafes have exactly one best score
        for(int i = 0; i < dept; i++) { //TODO: geht nicht, dummheit am abend
            //give the score from each child to their parents
            //by choosing the best score
            giveScoreToParent(tree, lastChildren);
        }

        tree.getSubTrees().forEach(child -> {//make sure every root leaf have a final score
            if (child.getHead().getScores().size()==1) {
                System.err.println("Something went wrong");
                System.exit(-1);
            }
        });

        //TODO: finally choose the best score from direkt root leafs and return the point
        //make every root leaf gives their score to the root and call bestScore on root
        //then search in root leafs to find the note with the choosen best score
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
        final Grid rootGrid = root.getHead().getGrid(); //gets the root grid
        Point[] markTypesNull = rootGrid.getAllOfMarkType(null); //get all empty points

        for (Point point : markTypesNull) {
            Tree<Node> childTree = root.addLeaf(new Node( rootGrid.copyWith(point, player), player)); //create new children from root with new generated head
            generateTreeForRoot(childTree, Mark.invert(player)); //do again with child
        }
    }

    //done
    public List<Node> getLastChildren(Tree<Node> root, List<Node> nodes) {
        root.getSubTrees().forEach(child -> {
            if (child.getSubTrees().size() == 0) {
                nodes.add(child.getHead());
            }
            getLastChildren(child, nodes);
        });
        return nodes;
    }

    private void giveScoreToParent(Tree<Node> tree, List<Node> lastChildren) {
        IntStream.range(0, lastChildren.size()) //gebe den best score von jedem children an den parent weiter
                .filter(i -> lastChildren.get(i).getParent(tree) != null) //the root node does not have a parent
                .forEach(i -> lastChildren.get(i) //null pointer if parent = null
                        //tree.getParent() in NOde
                        //return parent in Tree
                        //private Tree<T> parent = null;
                        .getParent(tree).getHead().addScore(bestScore(lastChildren.get(i))));
    }

    //calculates the score depends on how deep the node is saved in the tree
    private int calculateScore(Node node) { return node.getMinimax()==Mark.opponent ? 1 : -1*(calculateEmptyFields(node)+1); }

    //calculates the empty fields
    private int calculateEmptyFields(Node node) { return (int) IntStream.range(0, node.getGrid().getAllOfMarkType(null).length).count(); }

    //choose the best score for maximize or minimize
    private int bestScore(Node node) { return node.getMinimax()==Mark.opponent ? Collections.max(node.getScores()) : Collections.min(node.getScores()); }

        /*
        Berechne für jedes Spielende den Score, dann gebe alle Scores nach oben weiter
        bei mimimize wird der schlechteste Score weitergegeben
        bei maximize der beste
         */

        /*
           Schaue ob Node.getMinimax() false oder true gibt (minimize oder maximize)
           dann gebe den jeweiligen 'bestenScore' an den Parent weiter mit Node.setMinimax()
           */
}