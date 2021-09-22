package controller;

import domain.Grid;
import domain.Mark;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static util.GridUtils.getGridFromString;

class GridHistoryTest {
    @ParameterizedTest
    @MethodSource()
    void getActorToHistoryRecordReturnsCorrectActor(Grid grid1, Grid grid2, Mark actor) {
        final List<Grid> gridList = Arrays.stream(new Grid[]{grid1, grid2}).toList();
        final GridHistory gridHistory = new GridHistory(gridList);

        final Mark actorResult = gridHistory.getActorToHistoryRecord(1);
        assertEquals(actorResult, actor);
    }

    static Stream<Arguments> getActorToHistoryRecordReturnsCorrectActor() {
        return Stream.of(
                historySituation(
                        """
                                ∙ | O | X
                                O | ∙ | X
                                ∙ | ∙ | O
                                """,
                        """
                                ∙ | O | X
                                O | O | X
                                ∙ | ∙ | O
                                """,
                        Mark.opponent
                ),
                historySituation(
                        """
                                X | ∙ | X
                                X | ∙ | ∙
                                O | O | ∙
                                """,
                        """
                                X | X | X
                                X | ∙ | ∙
                                O | O | ∙
                                """,
                        Mark.self
                ),
                historySituation(
                        """
                                O | O | ∙
                                O | X | X
                                ∙ | X | X
                                """,
                        """
                                O | O | ∙
                                O | X | X
                                O | X | X
                                """,
                        Mark.opponent
                ),
                historySituation(
                        """
                                ∙ | ∙ | ∙
                                ∙ | ∙ | ∙
                                ∙ | ∙ | ∙
                                """,
                        """
                                ∙ | ∙ | ∙
                                ∙ | X | ∙
                                ∙ | ∙ | ∙
                                """,
                        Mark.self
                )
        );
    }

    static Arguments historySituation(String rawGrid1, String rawGrid2, Mark actor) {
        return Arguments.of(
                getGridFromString(rawGrid1),
                getGridFromString(rawGrid2),
                actor
        );
    }
}