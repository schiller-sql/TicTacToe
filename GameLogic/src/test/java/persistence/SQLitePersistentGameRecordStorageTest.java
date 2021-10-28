package persistence;

import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class SQLitePersistentGameRecordStorageTest extends PersistentGameRecordStorageTest {
    Connection connection;

    @BeforeEach
    void init() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:");
    }

    @Override
    PersistentGameRecordStorage getPersistentGameRecordStorage() throws SQLException {
        return new SQLitePersistentGameRecordStorage(connection, false);
    }
}