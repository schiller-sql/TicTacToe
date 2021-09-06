package domain;

/**
 * Represents a point in a 3 by 3 tic-tac-toe field
 */
public record Point(int x, int y) {
    public Point {
        assert (x >= 0 && x < 3);
        assert (y >= 0 && y < 3);
    }
}
