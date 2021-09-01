package opponent;

import domain.Point;
import domain.Field;

/**
 * Implements the Opponent interface in a random based way,
 * this is the most simple implementation of the Opponent
 */
public class RandomOpponent implements Opponent {
    @Override
    public Point move(Field field) {
        Point randomPoint;
        do {
            randomPoint = new Point(getRandomPosition(), getRandomPosition());
        } while(!field.markIsEmpty(randomPoint));
        return randomPoint;
    }

    private int getRandomPosition() {
        return (int) (Math.random() * 3);
    }
}