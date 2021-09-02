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
    // TODO: Do the checks if the game has been won, lost, or there has been a tie
    return GameState.running;
  }
}
