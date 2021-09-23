package domain;

import java.util.Arrays;
import java.util.Objects;

public record Row(int firstX, int firstY, int secondX, int secondY) {
    public Point[] toPoints() {
        return new Point[]{
                new Point(firstX, firstY),
                getMiddle(),
                new Point(secondX, secondY),
        };
    }

    public Point getMiddle() {
        return new Point(getMiddleX(), getMiddleY());
    }

    public int getMiddleX() {
        return (firstX + secondX) / 2;
    }

    public int getMiddleY() {
        return (firstY + secondY) / 2;
    }

    static Row fromTwoPoints(Point point1, Point point2) {
        return new Row(point1.x(), point1.y(), point2.x(), point2.y());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Row row = (Row) o;
        return firstX == row.firstX && firstY == row.firstY && secondX == row.secondX && secondY == row.secondY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstX, firstY, secondX, secondY);
    }

    @Override
    public String toString() {
        return "Row" + Arrays.toString(toPoints());
    }
}
