package controller;

import domain.Point;
import opponent.Opponent;

/**
 * Controls a 3 by 3 tic-tac-toe field,
 * if someone calls setPoint,
 * the field will be updated,
 * via the players input, the opponents response and
 * it will be checked if everything if the game has ended or
 * if someone has won
 */
public class Controller {
  private final Opponent opponent;
  private final MutableField field;

  /**
   * @param opponent The opponent (from the opponent interface)
   *                 that the player should face.
   *                 It is reasonable for the the countermove,
   *                 against the player
   */
  public Controller(Opponent opponent) {
    this.opponent = opponent;
    this.field = new MutableField();
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
    final Point opponentPoint = opponent.move(field);
    field.setMarkOpponent(opponentPoint);
    return new Move(null, field.getField());
  }
}
