package controller;

import domain.Field;
import domain.Mark;
import domain.Point;

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