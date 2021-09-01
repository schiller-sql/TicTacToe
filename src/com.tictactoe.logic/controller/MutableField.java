package com.tictactoe.logic.controller;

import com.tictactoe.logic.domain.Point;
import com.tictactoe.logic.domain.Field;
import com.tictactoe.logic.domain.Mark;

/**
 * Mutable version of the Field class,
 * so the controller can edit the field
 *
 * Should only be used in the controller,
 * as in the opponent for example,
 * it returns the point to be set
 */
class MutableField extends Field {

    public void setMarkOpponent(Point point) {
        assert(markIsEmpty(point));
        field[point.x()][point.y()] = Mark.opponent;
    }

    public void setMarkSelf(Point point) {
        assert(markIsEmpty(point));
        field[point.x()][point.y()] = Mark.self;
    }
}