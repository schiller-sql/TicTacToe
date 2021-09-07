package controller;

import domain.Grid;
import domain.Mark;
import domain.Point;
import opponent.Opponent;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Controls a 3 by 3 tic-tac-toe grid,
 * if someone calls setPoint,
 * the grid will be updated,
 * via the players input, the opponents' response,
 * it will be checked if everything is the game has ended or
 * if someone has won
 */
public class GameController {
    private final Opponent opponent;
    private final MutableGrid grid;
    private GameState state;

    /**
     * @param opponent The opponent (from the Opponent interface)
     *                 that the player should face.
     *                 It is responsible for the countermove,
     *                 against the player
     */
    public GameController(Opponent opponent) {
        this.opponent = opponent;
        this.grid = new MutableGrid();
        state = GameState.running;
    }

    /**
     * @param opponent     The opponent responsible for the countermove
     * @param startingGrid The starting grid data, the grid should have
     */
    public GameController(Opponent opponent, Mark[][] startingGrid) {
        assert (startingGrid.length == 3);
        assert (startingGrid[0].length == 3);
        assert (startingGrid[1].length == 3);
        assert (startingGrid[2].length == 3);

        this.opponent = opponent;
        this.grid = new MutableGrid(startingGrid);
        state = calculateGameState();
    }

    /**
     * Updates the state of the grid with the players cross
     * and the opponents response
     * <p>
     * Also finds out if anyone has won,
     * lost or if there was a tie
     * <p>
     * Only works if the state is still GameState.running
     * <p>
     * Information of the game state
     * is updated inside the controller
     *
     * @param point The point where the player sets his cross
     */
    public void setPoint(Point point) {
        assert (state == GameState.running);

        grid.setMarkSelf(point);
        state = calculateGameState();
        if (state != GameState.running)
            return;

        final Point opponentPoint = opponent.move(grid);
        grid.setMarkOpponent(opponentPoint);
        state = calculateGameState();
    }

    /**
     * @return Current GameState of the GameController
     */
    public GameState getState() {
        return state;
    }

    /**
     * @return Current Grid of the GameController
     */
    public Grid getGrid() {
        return grid;
    }

    /**
     * @return The current GameState
     */
    private GameState calculateGameState() {
        if (checkWonOrLost(Mark.self)) {
            return GameState.won;
        } else if (checkWonOrLost(Mark.opponent)) {
            return GameState.lost;
        } else if (checkTie()) {
            return GameState.tie;
        }
        return GameState.running;
    }

    private boolean checkTie() {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (grid.markIsEmpty(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkWonOrLost(Mark mark) {
        return checkForDiagonals(mark) || checkForLines(mark);
    }

    private boolean checkForDiagonals(Mark mark) {
        final boolean middleIs = grid.getMark(1, 1) == mark;
        final boolean firstDiagonal = grid.getMark(0, 0) == mark && grid.getMark(2, 2) == mark;
        final boolean secondDiagonal = grid.getMark(0, 2) == mark && grid.getMark(2, 0) == mark;
        return middleIs && (firstDiagonal || secondDiagonal);
    }

    private boolean checkForLines(Mark mark) {
        return checkForLine(mark, true) || checkForLine(mark, false);
    }

    private boolean checkForLine(Mark mark, boolean vertical) {
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

    /**
     * Mutable version of the Grid class,
     * so the controller can edit the grid
     * <p>
     * Should only be used in the controller,
     * as in the opponent for example,
     * it returns the point to be set
     */
    static class MutableGrid implements Grid {

        private final Mark[][] grid;


        /**
         * Empty constructor, with a clean grid
         */
        MutableGrid() {
            grid = new Mark[3][3];
        }

        /**
         * Constructor with a custom starting grid
         *
         * @param grid The starting grid
         */
        MutableGrid(Mark[][] grid) {
            this.grid = grid;
        }


        /**
         * Check if a field is empty by its Point
         *
         * @param point Which field to look at
         * @return True if the point is empty
         */
        public boolean markIsEmpty(Point point) {
            return getGrid()[point.y()][point.x()] == null;
        }

        /**
         * Check if a field is empty by its coordinates
         *
         * @param x The x coordinate
         * @param y The y coordinate
         * @return True if the point is empty
         */
        public boolean markIsEmpty(int x, int y) {
            return getGrid()[y][x] == null;
        }

        /**
         * Get a Mark in the grid by its Point
         *
         * @param point The Point at which the mark is at
         * @return The Mark which is searched for
         */
        public Mark getMark(Point point) {
            return getGrid()[point.y()][point.x()];
        }

        /**
         * Get all positions of a mark type in this Grid
         *
         * @param type The type of Mark to look for,
         *             null means looking for an empty field
         * @return All positions as a Point array
         */
        // TODO: Fix the warning by IntelliJ
        public Point[] getAllOfMarkType(Mark type) {
            ArrayList<Point> foundPoints = new ArrayList<>();
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    if (grid[y][x] == type) {
                        foundPoints.add(new Point(x, y));
                    }
                }
            }
            return foundPoints.toArray((Point[]) Array.newInstance(Point.class, foundPoints.size()));
        }

        /**
         * Get a Mark in the grid by its coordinates
         *
         * @param x The x coordinate
         * @param y The y coordinate
         * @return The Mark which is searched for
         */
        public Mark getMark(int x, int y) {
            return getGrid()[y][x];
        }

        /**
         * Set an opponent Mark
         *
         * @param point Where to set the opponent Mark
         */
        public void setMarkOpponent(Point point) {
            assert (markIsEmpty(point));
            grid[point.y()][point.x()] = Mark.opponent;
        }

        /**
         * Set a self Mark
         *
         * @param point Where to set the self Mark
         */
        public void setMarkSelf(Point point) {
            assert (markIsEmpty(point));
            grid[point.y()][point.x()] = Mark.self;
        }


        @Override
        public Mark[][] getGrid() {
            return grid;
        }
    }
}
