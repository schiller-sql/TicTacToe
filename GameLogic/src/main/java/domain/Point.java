package domain;

import java.util.Objects;

/**
 * Represents a point in a 3 by 3 tic-tac-toe field
 */
public record Point(int x, int y) {
    public Point {
        assert (x >= 0 && x < 3);
        assert (y >= 0 && y < 3);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
