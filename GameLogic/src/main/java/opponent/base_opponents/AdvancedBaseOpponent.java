package opponent.base_opponents;

import domain.Grid;
import domain.Mark;
import domain.Point;
import opponent.Opponent;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Opponents can make this their superclass,
 * if they want to have access to 'advanced' methods,
 * to help in creating a tic-tac-toe opponent
 */
public abstract class AdvancedBaseOpponent extends Opponent {
    /**
     * Get an array of Points back,
     * which are the last Point needed to complete a row
     * <p>
     * This means the Mark on the Point is null
     *
     * @param grid     The gridData you want the locations from
     * @param markType Whose last Points should be searched for (self/opponent)
     * @return Array of Points, that need
     */
    public Point[] getRowFinalNeededPoints(Grid grid, Mark markType) {
        Stream<Field[]> rows = gridToRowStream(grid);
        rows = rows.filter(
                fields -> rowContainsMarks(
                        fields, new Mark[]{
                                markType,
                                markType,
                                null,
                        }
                )
        );
        final Stream<Point> finalNeedPoints = rows.map(fields -> {
            for (Field field : fields) {
                if (field.mark == null) {
                    return field.toPoint();
                }
            }
            throw new Error("All fields where no mark is null, should have been sorted out");
        });
        return pointStreamToArray(finalNeedPoints);
    }

    private boolean rowContainsMarks(Field[] row, Mark[] pattern) {
        final boolean[] patternMarkChecker = new boolean[3];
        row:
        for (int i = 0; i < 3; i++) {
            final Mark matchingFieldMark = row[i].mark;
            for (int j = 0; j < 3; j++) {
                if (matchingFieldMark == pattern[j] && !patternMarkChecker[j]) {
                    patternMarkChecker[j] = true;
                    continue row;
                }
            }
            return false;
        }
        return true;
    }

    private Stream<Field[]> gridToRowStream(Grid grid) {
        final Field[][] fields = new Field[8][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                fields[i * 2][j] = new Field(j, i, grid.getMark(j, i));
                fields[i * 2 + 1][j] = new Field(i, j, grid.getMark(i, j));
            }
        }
        fields[6][0] = Field.fromGrid(grid, 0, 0);
        fields[6][1] = Field.fromGrid(grid, 1, 1);
        fields[6][2] = Field.fromGrid(grid, 2, 2);

        fields[7][0] = Field.fromGrid(grid, 0, 2);
        fields[7][1] = Field.fromGrid(grid, 1, 1);
        fields[7][2] = Field.fromGrid(grid, 2, 0);

        return Arrays.stream(fields);
    }

    private Point[] pointStreamToArray(Stream<Point> stream) {
        return stream.toArray(Point[]::new);
    }

    private Field[][] fieldStreamToArray(Stream<Field[]> stream) {
        return stream.toArray(Field[][]::new);
    }

    private record Field(int x, int y, Mark mark) {
        private Field {
            assert (x >= 0 && x < 3);
            assert (y >= 0 && y < 3);
        }

        private static Field fromGrid(Grid grid, int x, int y) {
            return new Field(x, y, grid.getMark(x, y));
        }

        Point toPoint() {
            return new Point(x, y);
        }
    }
}
