package opponent;

import domain.Grid;
import domain.Mark;
import domain.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.GridUtils.gridIsFull;
import static util.MarkUtils.markFromInt;

public abstract class OpponentTest {
    private static final List<Grid> allPossibleSituations = new ArrayList<>();

    static {
        for (int a = 0; a < 3; a++) {
            for (int b = 0; b < 3; b++) {
                for (int c = 0; c < 3; c++) {
                    for (int d = 0; d < 3; d++) {
                        for (int e = 0; e < 3; e++) {
                            for (int f = 0; f < 3; f++) {
                                for (int g = 0; g < 3; g++) {
                                    for (int h = 0; h < 3; h++) {
                                        for (int i = 0; i < 3; i++) {
                                            final Mark[][] gridData = new Mark[][]{
                                                    rowFromInts(a, b, c),
                                                    rowFromInts(d, e, f),
                                                    rowFromInts(g, h, i),
                                            };
                                            final Grid grid = new Grid(gridData);
                                            if (!gridIsFull(grid)) {
                                                allPossibleSituations.add(grid);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected abstract Opponent getOpponent();

    protected Opponent opponent;

    @BeforeEach
    protected void init() {
        this.opponent = getOpponent();
    }

    @ParameterizedTest
    @MethodSource("moveGivesBackValidPointIndependentFromWhichSituationSource")
    void moveGivesBackValidPointIndependentFromWhichSituation(Grid situation) {
        final Point move = opponent.move(situation);

        assertTrue(situation.markIsEmpty(move));
    }

    static private Stream<Grid> moveGivesBackValidPointIndependentFromWhichSituationSource() {
        // 19,171 possible combinations values
        return allPossibleSituations.stream();
    }

    static private Mark[] rowFromInts(int i1, int i2, int i3) {
        return new Mark[]{
                markFromInt(i1),
                markFromInt(i2),
                markFromInt(i3),
        };
    }
}
