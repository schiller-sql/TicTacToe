package controller;

import domain.Mark;
import domain.Point;
import opponent.Opponent;

/**
 * Controls a 3 by 3 tic-tac-toe field,
 * if someone calls setPoint,
 * via the players input, the opponents response and
 * it will be checked if everything if the game has ended or
 * it will be checked if everything is the game has ended or
 * if someone has won
 */
public class Controller {
  private final Opponent opponent;
  private final MutableField field;
  private final MutableGrid grid;
  private GameState state;

  /**
   *                 that the player should face.
   *                 It is responsible for the countermove,
   *                 against the player
   */
  public Controller(Opponent opponent) {
    this.opponent = opponent;
    this.field = new MutableField();
    state = GameState.running;
  }

  /**
   * @param opponent The opponent responsible for the countermove
   * @param startingGrid The starting grid data, the grid should have
   */
  public GameController(Opponent opponent, Mark[][] startingGrid) {
    assert(startingGrid[0].length == 3);
    assert(startingGrid[1].length == 3);
    this.opponent = opponent;
    this.field = new MutableField();
    this.grid = new MutableGrid(startingGrid);
    state = calculateGameState();
  }

  /**
   * Updates the state of the field with the players cross
   * and the opponents response
   *
   * Also finds out if anyone has won,
   * lost or if there was a tie
   *
   * @param point The point where the player sets his cross
   * @return The updated state of the field,
   * with the winning, losing, or tie information
   */
  public Move setPoint(Point point) {
    // TODO: Do the checks if the game has been won, lost, or there has been a tie
    field.setMarkSelf(point);
    final EndGameState endGameState = checkForEndOfGame();
    final Point opponentPoint = opponent.move(field);
    field.setMarkOpponent(opponentPoint);
    return new Move(null, field.getField());
  }

  /**
   * @return The EndGameState if the game ended,
   * is null if the game hasn't ended
   */
  public EndGameState checkForEndOfGame() {
    return null;
  }
}
