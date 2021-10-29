package persistence;

import controller.GameController;
import controller.GameState;
import domain.Grid;
import domain.GridHistory;
import opponent.Opponent;

/**
 * An abstract class for storing GameRecords,
 * which represent a TicTacToe game
 */
public abstract class PersistentGameRecordStorage {

    /**
     * Gives back all GameRecords currently in the storage
     *
     * @return The ordered GameRecords
     */
    public abstract GameRecord[] getCachedGameRecords();

    /**
     * Create a GameRecord
     * from a GridHistory, a GameState, and an Opponent
     *
     * @return The newly created GameRecord
     */
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

    /**
     * Create a GameRecord from a GameController
     *
     * @return The newly created GameRecord
     */
    public GameRecord addGameRecord(GameController controller) throws GameRecordStorageException {
        return addGameRecord(
                controller.getHistory(),
                controller.getState(),
                controller.getOpponent()
        );
    }

    /**
     * Delete a GameRecord from the storage permanently
     *
     * @param gameRecord which gameRecord to delete
     */
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

    /**
     * Count the amount of GameRecords in a certain GameState
     *
     * @param gameState Which GameState to count
     */
    public int countRecordsWithGameState(GameState gameState) {
        int i = 0;
        for (GameRecord record : getCachedGameRecords()) {
            if (record.getCurrentState() == gameState) {
                i++;
            }
        }
        return i;
    }

    public int wins() {
        return countRecordsWithGameState(GameState.won);
    }

    public int loses() {
        return countRecordsWithGameState(GameState.lost);
    }

    public int ties() {
        return countRecordsWithGameState(GameState.tie);
    }

    public int running() {
        return countRecordsWithGameState(GameState.running);
    }

    public int total() {
        return getCachedGameRecords().length;
    }
}
