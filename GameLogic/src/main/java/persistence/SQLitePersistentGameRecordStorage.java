package persistence;

import controller.GameState;
import domain.Grid;
import domain.GridHistory;
import domain.Mark;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLitePersistentGameRecordStorage extends PersistentGameRecordStorage {
    Connection connection;
    Statement statement;
    final boolean logInformation;
    private final List<GameRecord> data = new ArrayList<>();

    public SQLitePersistentGameRecordStorage(String fileName) throws SQLException {
        assert (fileName.matches("^(?!\\s).*\\.db$")) : "Filename must end with '.db'";

        connection = DriverManager.getConnection("jdbc:sqlite:" + fileName);
        initDB();
        logInformation = true;
    }

    public SQLitePersistentGameRecordStorage(Connection sqliteConnection) throws SQLException {
        connection = sqliteConnection;
        initDB();

        logInformation = true;
    }

    public SQLitePersistentGameRecordStorage(Connection sqliteConnection, boolean logInformation) throws SQLException {
        connection = sqliteConnection;
        initDB();

        this.logInformation = logInformation;
    }

    private void initDB() throws SQLException {
        statement = connection.createStatement();
        statement.setQueryTimeout(20);

        // Stats
//        statement.executeUpdate("create table if not exists stats (" +
//                                "wins integer not null," +
//                                "loses integer not null," +
//                                "ties integer not null" +
//                                ")");
//        statement.executeUpdate("insert into stats values (0, 0, 0)");

//        final ResultSet statsResult = statement.executeQuery("select * from stats");
//        winAmount = statsResult.getInt("wins");
//        loseAmount = statsResult.getInt("loses");
//        tieAmount = statsResult.getInt("ties");
//        statsResult.close();

        // Games
        statement.executeUpdate("create table if not exists games (" +
                                "id integer primary key autoincrement," +
                                "updatedAt timestamp not null default current_timestamp," +
                                "opponent varchar not null," +
                                "gameState varchar not null default 'running'," +
                                "lastData char not null default '         ' check (length(lastData) <= 9), " +
                                "constraint onlyCorrectGamesStates " +
                                "  check (" +
                                "    (gameState = 'running') or " +
                                "    (gameState = 'won') or " +
                                "    (gameState = 'tie') or " +
                                "    (gameState = 'lost')" +
                                "  )" +
                                ")");
        statement.executeUpdate("create index if not exists newestGames on games(updatedAt)");

        statement.executeUpdate("create table if not exists gameHistory (" +
                                "gameId integer not null references games(id) on delete cascade," +
                                "position int2 not null check (position > 0 and position <= 9)," +
                                "data char not null check (length(data) <= 9)" +
                                ")");

        final ResultSet result = statement.executeQuery("select * from games order by updatedAt, id desc");
        while (result.next()) {
            data.add(gameRecordFromSingleRowResult(result));
        }

        logInformation("Finished");
    }

    public GameRecord[] getCachedGameRecords() {
        return data.toArray(new GameRecord[0]);
    }

    @Override
    public GameRecord addGameRecord(
            GridHistory gridHistory,
            Grid currentGrid,
            GameState currentState,
            String opponentName
    ) throws GameRecordStorageException {
        try {
            final String sql = "insert into games(opponent, gameState, lastData)" +
                               "values (" +
                               "'" + opponentName + "'," +
                               "'" + gameStateToString(currentState) + "'," +
                               "'" + gridToString(currentGrid) + "'" +
                               ")" +
                               "returning *";
            final ResultSet result = statement.executeQuery(sql);
            final GameRecord newRecord = gameRecordFromSingleRowResult(result);
            result.close();

            data.add(0, newRecord);

            return newRecord;
        } catch (SQLException e) {
            throw new GameRecordStorageException(e.getMessage());
        }
    }

    @Override
    public void deleteGameRecord(int id) throws GameRecordStorageException {
        try {
            statement.executeUpdate("delete from games where id = " + id);
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getId() == id) {
                    data.remove(i);
                    return;
                }
            }
            throw new Error("Record has to be contained in the PersistentGameRecordStorage");
        } catch (SQLException e) {
            throw new GameRecordStorageException(e.getMessage());
        }
    }

    @Override
    void updateGameRecord(GameRecord updatedRecord) throws GameRecordStorageException {
        try {
            statement.executeUpdate("update games " +
                                    "set updatedAt = current_timestamp," +
                                    "opponent = '" + updatedRecord.getOpponent().getName() + "'," +
                                    "gameState = '" + gameStateToString(updatedRecord.getCurrentState()) + "'," +
                                    "lastData = '" + gridToString(updatedRecord.getCurrentGrid()) + "'" +
                                    "where id = " + updatedRecord.getId());
        } catch (SQLException e) {
            throw new GameRecordStorageException(e.getMessage());
        }
    }

    @Override
    GridHistory getGridHistory(int id) {
        return null;
    }

    @Override
    void updateGridHistory(int id, GridHistory gridHistory) {
        // TODO: update the list reihenfolge and the games with currentGrid
    }

    private GridHistory gridHistoryFromResult(ResultSet results) {
        return null;
    }

    private GameRecord gameRecordFromSingleRowResult(ResultSet resultSet) throws SQLException {
        return new GameRecord(
                this,
                resultSet.getInt("id"),
                resultSet.getDate("updatedAt"),
                null,
                gridFromString(resultSet.getString("lastData")),
                gameStateFromString(resultSet.getString("gameState")),
                resultSet.getString("opponent")
        );
    }

    private String gridToString(Grid grid) {
        StringBuilder s = new StringBuilder();
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                s.append(markToString(grid.getMark(x, y)));
            }
        }
        return s.toString();
    }

    private Grid gridFromString(String rawGrid) {
        Mark[][] gridData = new Mark[3][3];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                final char rawMark = rawGrid.charAt(x + (y * 3));
                gridData[y][x] = markFromChar(rawMark);
            }
        }
        return new Grid(gridData);
    }

    private String markToString(Mark mark) {
        if (mark == null) {
            return " ";
        }
        return switch (mark) {
            case self -> "X";
            case opponent -> "O";
        };
    }

    private Mark markFromChar(char rawMark) {
        return switch (rawMark) {
            case 'X' -> Mark.self;
            case 'O' -> Mark.opponent;
            case ' ' -> null;
            default -> throw new Error("Char '" + rawMark + "' can not be mapped to a Mark");
        };
    }

    private String gameStateToString(GameState gameState) {
        return switch (gameState) {
            case running -> "running";
            case won -> "won";
            case tie -> "tie";
            case lost -> "lost";
        };
    }

    private GameState gameStateFromString(String rawGameState) {
        return switch (rawGameState) {
            case "running" -> GameState.running;
            case "won" -> GameState.won;
            case "tie" -> GameState.tie;
            case "lost" -> GameState.lost;
            default -> throw new Error("String'" + rawGameState + "' can not be mapped to a GameState");
        };
    }

    private void logInformation(String message) {
        if (logInformation) {
            System.out.println("SQL: " + message);
        }
    }
}
