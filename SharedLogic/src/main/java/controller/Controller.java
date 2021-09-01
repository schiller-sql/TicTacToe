package controller;

import domain.Point;
import opponent.Opponent;

/**
 *
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
    field.setMarkSelf(point);
    final Point opponentPoint = opponent.move(field);
    field.setMarkOpponent(opponentPoint);
    return new Move(null, field.getField());
  }
}
