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
class MutableField implements Field {

    private final Mark[][] field;

    MutableField() {
        field = new Mark[3][3];
    }

    MutableField(Mark[][] field) {
        this.field = field;
    }

    public void setMarkOpponent(Point point) {
        assert(markIsEmpty(point));
        field[point.x()][point.y()] = Mark.opponent;
    }

    public void setMarkSelf(Point point) {
        assert(markIsEmpty(point));
        field[point.x()][point.y()] = Mark.self;
    }

    @Override
    public Mark[][] getField() {
        return field;
    }
}