package opponent;

import domain.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MinimaxOpponent extends AdvancedBaseOpponent {
    ;

    @Override
    public Point move(Grid grid) {
        final Tree<Node> tree = new Tree<>(new Node(grid));
        generateTreeForRoot(tree, Mark.opponent);
        Mark maxFor = Mark.opponent, minFor = Mark.self;
        System.out.print(tree);
       // tree.printTreeAlternativ(new StringBuilder(), "", "");

        //checkPLayerWins(): position: null, int score= -1*(num-empty-squares()+1)
        //so the best score is returns for the earliest game stat the opponent can win, because by time there are less open fields
        //if tie(): position null, int schore

        /*
        if max() => set score to -Inf
        else min() => set score to +Inf
         */

        //copyWith() for all Mark==null
        //calculate the score for each

        /*save best new score
        basend on who the max_player is

        this effects that the min() returns the most negativ score
        and the max() the highest score
         */

        return new Point(0, 0);
    }

    public void generateTreeForRoot(Tree<Node> root, Mark player) {
        final Grid rootGrid = root.getHead().getGrid();
        Point[] markTypesNull = rootGrid.getAllOfMarkType(null);

        for (Point point : markTypesNull) { //at start should be 8
            final Grid childGrid = rootGrid.copyWith(point, player);
            final Tree<Node> childTree = root.addLeaf(new Node(childGrid, player));
            generateTreeForRoot(childTree, Mark.invert(player));
        }
    }

    public List<Tree<Node>> getLastChilds(Tree<Node> tree) {

        return null;
    }

    private int calculateScore() {

        return 0;
    }

    private int bestScore() {

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

        Solution 1:
        for each last_child:
        while(T != root) {
           this.getParent().setScore(bestScore())
        }

        Solution 2:
        gebe den wert von jedem last_child soweit nach oben, bis ein tree mehrere scores bekommt,
        dann berechne anhand von minimize/maximize den best_score und gebe diesen wieder so lange weiter
        bis ein parent tree wieder mehrere scores bekommt
         */

    }

}