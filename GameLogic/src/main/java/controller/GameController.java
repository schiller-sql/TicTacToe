package controller;

import domain.Grid;
import domain.Mark;
import domain.Point;
import opponent.Opponent;

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
   * @param opponent The opponent responsible for the countermove
   * @param startingGrid The starting grid data, the grid should have
   */
  public GameController(Opponent opponent, Mark[][] startingGrid) {
    assert(startingGrid.length == 3);
    assert(startingGrid[0].length == 3);
    assert(startingGrid[1].length == 3);
    assert(startingGrid[2].length == 3);

    this.opponent = opponent;
    this.grid = new MutableGrid(startingGrid);
    state = calculateGameState();
  }

  /**
   * Updates the state of the grid with the players cross
   * and the opponents response
   *
   * Also finds out if anyone has won,
   * lost or if there was a tie
   *
   * Only works if the state is still GameState.running
   *
   * Information of the game state
   * is updated inside the controller
   *
   * @param point The point where the player sets his cross
   */
  public void setPoint(Point point) {
    assert (state == GameState.running);

    grid.setMarkSelf(point);
    state = calculateGameState();
    if(state != GameState.running)
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
    if(checkWonOrLost(Mark.self)) {
      return GameState.won;
    } else if (checkWonOrLost(Mark.opponent)) {
      return GameState.lost;
    } else if (checkTie()) {
      return GameState.tie;
    }
    return GameState.running;
  }

  private boolean checkTie() {
    for(int x = 0; x < 3; x++) {
      for(int y = 0; y < 3; y++) {
        if(grid.markIsEmpty(x, y)) {
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
    final boolean middleIs = grid.markIs(1, 1, mark);
    final boolean firstDiagonal = grid.markIs(0, 0, mark) && grid.markIs(2, 2, mark);
    final boolean secondDiagonal = grid.markIs(0, 2, mark) && grid.markIs(2, 0, mark);
    return middleIs && (firstDiagonal || secondDiagonal);
  }

  private boolean checkForLines(Mark mark) {
    return checkForLine(mark, true) || checkForLine(mark, false);
  }

  private boolean checkForLine(Mark mark, boolean vertical) {
    xLoop: for(int x = 0; x < 3; x++) {
      for(int y = 0; y < 3; y++) {
        if(!grid.markIs(vertical ? x : y, vertical ? y : x, mark)) {
          continue xLoop;
        }
      }
      return true;
    }
    return false;
  }
}
