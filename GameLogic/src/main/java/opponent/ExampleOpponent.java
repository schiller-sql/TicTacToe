package opponent;

import domain.Grid;
import domain.Mark;
import domain.Point;

public class ExampleOpponent implements Opponent{

    @Override
    public Point move(Grid grid) {
        Point finalPoint;
        Point[] myPoints = grid.getAllOfMarkType(Mark.opponent);
        Point[] playerPoints = grid.getAllOfMarkType(Mark.self);
        Point[] freePoints = new Point[9];

        for(int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                Point temp = new Point(x,y);
                if(grid.markIsEmpty(temp)) {
                    freePoints[freePoints.length-1]=temp;
                }
            }
        }

        if(freePoints.length==1) { /**
         if only one Point is available...
         */
            return freePoints[0];
        }

        /**
         * to make sure, that a valid Point is return
         */
        finalPoint=freePoints[0];

        /**
         * Prüft zuerst, ob ein direkter Gewinn möglich ist,
         * ansonsten wird nach Möglichkeit neben einer Spielermakierung
         * zu platzieren
         */

        /**
         * Check if a direct win is possible
         * (does not check all possibilities)
         */

        boolean vertical = false;
        boolean horizontal = false;

        int winCondition = 0;
        /**
         * check horizontal
         */
        for(int i = 0; i < 3; i++) {
            if(grid.getMark(1,i)==Mark.opponent) {
                winCondition++;
                if(winCondition==2 ) {
                    if(grid.getMark(1,i+1)==null) {
                        finalPoint = new Point(1,i+1);
                        return finalPoint;
                    }
                }
            }
        } winCondition=0;

        /**
         * check vertical
         */
        for(int i = 0; i < 3; i++) {
            if(grid.getMark(i, 1)==Mark.opponent) {
                winCondition++;
                if(winCondition==2 ) {
                    if(grid.getMark(i+1, 1)==null) {
                        finalPoint = new Point(i+1,1);
                        return finalPoint;
                    }
                }
            }
        }

        /**
         * prüft nicht die diagonalen Punkte
         */
        Point[] surroundings = new Point[9];
        for(int i = 0; i < playerPoints.length; i++) {
            //prüft, ob der Punkt im grid liegt und noch verfügbar ist
            if(playerPoints[i].x()-1 >= 0 && grid.markIsEmpty(new Point(playerPoints[i].x()-1, playerPoints[i].y()))) {
                surroundings[surroundings.length]=new Point(playerPoints[i].x()-1, playerPoints[i].y());
            }

            //prüft, ob der Punkt im grid liegt und noch verfügbar ist
            if(playerPoints[i].x()+1 < 3 && grid.markIsEmpty(new Point(playerPoints[i].x()+1, playerPoints[i].y()))) {
                surroundings[surroundings.length]=new Point(playerPoints[i].x()+1, playerPoints[i].y());
            }

            //prüft, ob der Punkt im grid liegt und noch verfügbar ist
            if(playerPoints[i].y()-1 >= 0 && grid.markIsEmpty(new Point(playerPoints[i].x(), playerPoints[i].y()-1))) {
                surroundings[surroundings.length]=new Point(playerPoints[i].x(), playerPoints[i].y()-1);
            }

            //prüft, ob der Punkt im grid liegt und noch verfügbar ist
            if(playerPoints[i].y()+1 < 3 && grid.markIsEmpty(new Point(playerPoints[i].x(), playerPoints[i].y()+1))) {
                surroundings[surroundings.length]=new Point(playerPoints[i].x(), playerPoints[i].y()+1);
            }
        }
        if(surroundings.length<=0) {
            return surroundings[0];
        }

        return finalPoint;
    }
}
