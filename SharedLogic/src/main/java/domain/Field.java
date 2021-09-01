package domain;


/**
 * Represents a 3 by 3 tic-tac-toe field
 */
public class Field {
    protected Mark[][] field;

    public Field() {
        field = new Mark[3][3];
    }

    public Mark[][] getField() {
        return field;
    }

    public boolean markIsEmpty(Point point) {
        return field[point.x()][point.y()] == null;
    }

    public boolean markIs(Point point, Mark markType) {
        return field[point.x()][point.y()] == markType;
    }
}
