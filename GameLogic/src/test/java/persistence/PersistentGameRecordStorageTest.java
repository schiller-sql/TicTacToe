package persistence;

import controller.GameController;
import controller.GameState;
import domain.Grid;
import domain.GridHistory;
import opponent.Opponent;
import opponent.default_opponents.MinimaxOpponent;
import opponent.default_opponents.RandomOpponent;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static util.GridUtils.getGridFromString;

abstract class PersistentGameRecordStorageTest {
    /**
     * @implSpec All PersistentGameRecordStorages should be connected
     * to the same database for a Test,
     * as the persistence is tested,
     * if there is reliance on a connection of some sort,
     * it should be saved as an instance variable
     * and be reset on each test using @BeforeEach
     */
    abstract PersistentGameRecordStorage getPersistentGameRecordStorage() throws Exception;

    @Test
    void getTiesAmountPersistsData() throws Exception {
        final var expectedAmount = 10;

        for (int i = 0; i < expectedAmount; i++) {
            getPersistentGameRecordStorage().addGameRecord(
                    new GridHistory(Collections.singletonList(new Grid())),
                    GameState.tie,
                    new RandomOpponent()
            );
        }

        assertEquals(expectedAmount, getPersistentGameRecordStorage().countRecordsWithGameState(GameState.tie));
    }

    @Test
    void addingUpdatingDeletingGameRecordPersistsData() throws Exception {
        // First GameRecord
        final var firstExpectedOpponent = new RandomOpponent();
        final var firstExpectedGameState = GameState.won;
        final var firstExpectedGrid = getGridFromString("""
                O | X | ∙
                X | ∙ | X
                X | X | ∙
                """);

        // Second GameRecord
        var secondUnfinishedExpectedGrid = getGridFromString("""
                X | O | O
                O | X | X
                X | O | O
                """);
        final var secondExpectedOpponent = new MinimaxOpponent();
        final var secondController = new GameController(secondExpectedOpponent, secondUnfinishedExpectedGrid);
        final var secondExpectedGameState = secondController.getState();
        final var secondExpectedGrid = secondController.getGrid();

        // Insert and check first record
        final var firstRecordDirectly = getPersistentGameRecordStorage().addGameRecord(
                new GridHistory(Collections.singletonList(firstExpectedGrid)),
                firstExpectedGameState,
                firstExpectedOpponent
        );
        assertRecord(firstExpectedGrid, firstExpectedGameState, firstExpectedOpponent, firstRecordDirectly);

        final var records = getPersistentGameRecordStorage().getCachedGameRecords();
        assertEquals(1, records.length);

        final var firstRecord = records[0];
        assertRecord(firstExpectedGrid, firstExpectedGameState, firstExpectedOpponent, firstRecord);

        // Insert second record and check both records
        final var secondRecordDirectly = getPersistentGameRecordStorage().addGameRecord(secondController);
        assertRecord(secondExpectedGrid, secondExpectedGameState, secondExpectedOpponent, secondRecordDirectly);

        final var records2 = getPersistentGameRecordStorage().getCachedGameRecords();
        assertEquals(2, records2.length);

        final var firstRecord2 = records2[1]; // First record now on second spot
        final var secondRecord2 = records2[0];
        assertRecord(firstExpectedGrid, firstExpectedGameState, firstExpectedOpponent, firstRecord2);
        assertRecord(secondExpectedGrid, secondExpectedGameState, secondExpectedOpponent, secondRecord2);

        // Update gameState and grid of firstRecord
        final var newFirstExpectedGameState = GameState.lost;
        final var newFirstExpectedGrid = new Grid();
        firstRecord2.setCurrentState(newFirstExpectedGameState);
        firstRecord2.setCurrentGrid(newFirstExpectedGrid);

        assertRecord(newFirstExpectedGrid, newFirstExpectedGameState, firstExpectedOpponent, firstRecord2);

        final var records3 = getPersistentGameRecordStorage().getCachedGameRecords();
        assertEquals(2, records3.length);

        final var firstRecord3 = records3[1]; // First record now on second spot
        final var secondRecord3 = records3[0];
        assertRecord(newFirstExpectedGrid, newFirstExpectedGameState, firstExpectedOpponent, firstRecord3);
        assertRecord(secondExpectedGrid, secondExpectedGameState, secondExpectedOpponent, secondRecord3);

        // Delete first record
        getPersistentGameRecordStorage().deleteGameRecord(firstRecord2);

        final var records4 = getPersistentGameRecordStorage().getCachedGameRecords();
        assertEquals(1, records4.length);

        assertRecord(secondExpectedGrid, secondExpectedGameState, secondExpectedOpponent, records4[0]);

        // Delete second record
        getPersistentGameRecordStorage().deleteGameRecord(secondRecord2);

        final var records5 = getPersistentGameRecordStorage().getCachedGameRecords();
        assertEquals(0, records5.length);
    }

    static void assertRecord(
            Grid expectedGrid,
            GameState expectedGameState,
            Opponent expectedOpponent,
            GameRecord actual
    ) {
        assertEquals(expectedGrid, actual.getCurrentGrid());
        assertEquals(expectedGameState, actual.getCurrentState());
        assertEquals(expectedOpponent.getName(), actual.getOpponent().getName());
    }
}