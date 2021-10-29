package persistence;

import controller.GameController;
import controller.GameState;
import domain.Grid;
import domain.GridHistory;
import opponent.Opponent;

public abstract class PersistentGameRecordStorage {

    public abstract GameRecord[] getCachedGameRecords();

    public GameRecord addGameRecord(
            GridHistory gridHistory,
            GameState currentState,
            Opponent opponent
    ) throws GameRecordStorageException {
        return addGameRecord(
                gridHistory,
                gridHistory.getLastHistoryRecord(),
                currentState,
                opponent.getName()
        );
    }

    public GameRecord addGameRecord(GameController controller) throws GameRecordStorageException {
        return addGameRecord(
                controller.getHistory(),
                controller.getState(),
                controller.getOpponent()
        );
    }

    public void deleteGameRecord(GameRecord gameRecord) throws GameRecordStorageException {
        deleteGameRecord(gameRecord.getId());
    }

    /*
        All methods after this should stay package private,
        as the id and updating system is not meant to be exposed
     */
    abstract GameRecord addGameRecord(
            GridHistory gridHistory,
            Grid currentGrid,
            GameState currentState,
            String opponentName
    ) throws GameRecordStorageException;

    abstract void deleteGameRecord(int id) throws GameRecordStorageException;

    abstract void updateGameRecord(GameRecord updatedRecord) throws GameRecordStorageException;

    abstract GridHistory getGridHistory(int id) throws GameRecordStorageException;

    /**
     * @implSpec The lastData is handled inside the GameRecord by it calling the updateGameRecord
     */
    abstract void updateGridHistory(int id, GridHistory gridHistory) throws GameRecordStorageException;

    public int countRecordsWithGameState(GameState gameState) {
        int i = 0;
        for (GameRecord record : getCachedGameRecords()) {
            if (record.getCurrentState() == gameState) {
                i++;
            }
        }
        return i;
    }
}
