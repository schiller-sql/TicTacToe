package com.tictactoe.logic.controller;

import com.tictactoe.logic.domain.Point;
import com.tictactoe.logic.opponents.Opponent;

public class Controller {
  private final Opponent opponent;
  private final MutableField field;

  public Controller(Opponent opponent) {
    this.opponent = opponent;
    this.field = new MutableField();
  }

  public Move setPoint(Point point) {
    field.setMarkSelf(point);
    final Point opponentPoint = opponent.move(field);
    field.setMarkOpponent(opponentPoint);
    return new Move(null, field.getField());
  }
}
