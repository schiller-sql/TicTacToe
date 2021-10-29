package persistence;

import controller.GameController;
import controller.GameState;
import domain.Grid;
import domain.GridHistory;
import opponent.Opponent;

import java.util.Date;

/**
 * Represents a TicTacToe game for the PersistentGameRecordStorage
 */
public final class GameRecord {
    private final PersistentGameRecordStorage storage;
    private final int id;
    private Date lastUpdate;
    private GridHistory history;
    private Grid currentGrid;
    private GameState currentState;
    private Opponent opponent;

    GameRecord(
            PersistentGameRecordStorage storage,
            int id,
            Date lastUpdate,
            GridHistory history,
            Grid currentGrid,
            GameState currentState,
            String opponentName
    ) {
        this.storage = storage;
        this.id = id;
        this.lastUpdate = lastUpdate;
        this.currentState = currentState;
        this.currentGrid = currentGrid;
        this.history = history;

        for (Opponent opponent : Opponent.defaultOpponents()) {
            if (opponent.getName().equals(opponentName)) {
                this.opponent = opponent;
            }
        }

        assert (opponent != null) : "Matching Opponent not found";
    }

    /**
     * Should be package private, as the id should not be
     * exposed to anyone except the PersistentStorageException
     */
    int getId() {
        return id;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public Grid getCurrentGrid() {
        return currentGrid;
    }

    public Opponent getOpponent() {
        return opponent;
    }

    public GridHistory getHistory() throws GameRecordStorageException {
        if (history == null) {
            history = storage.getGridHistory(id);
        }
        return history;
    }


    public void setCurrentState(GameState currentState) throws GameRecordStorageException {
        this.currentState = currentState;
        saveUpdate();
    }

    public void setOpponent(Opponent opponent) throws GameRecordStorageException {
        this.opponent = opponent;
        saveUpdate();
    }

    public void setCurrentGrid(Grid currentGrid) throws GameRecordStorageException {
        this.currentGrid = currentGrid;
        saveUpdate();
    }

    public void setHistory(GridHistory history) throws GameRecordStorageException {
        currentGrid = history.getLastHistoryRecord();
        saveUpdate();
        storage.updateGridHistory(id, history);
        this.history = history;
    }

    public void updateWithController(GameController controller) throws GameRecordStorageException {
        setHistory(controller.getHistory());
        currentGrid = controller.getGrid();
        currentState = controller.getState();
        opponent = controller.getOpponent();
        saveUpdate();
    }

    private void saveUpdate() throws GameRecordStorageException {
        storage.updateGameRecord(this);
        lastUpdate = new Date();
    }
}
