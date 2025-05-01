package database.functions;

import database.DatabaseManager;
import database.State;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class MoveTool {
    private static final String MOVE_SQL =
            "CALL move(?, ?, ?, ?, ?, ?, ?, ?)";

    public static void moveSelection(int x1, int y1, int x2, int y2,
                                     int newX1, int newY1, int newX2, int newY2) {
        long startTime = System.nanoTime();

        try (Connection connection = DatabaseManager.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(MOVE_SQL)) {

                // Set original area coordinates
                stmt.setInt(1, x1);
                stmt.setInt(2, y1);
                stmt.setInt(3, x2);
                stmt.setInt(4, y2);

                // Set new position coordinates
                stmt.setInt(5, newX1);
                stmt.setInt(6, newY1);
                stmt.setInt(7, newX2);
                stmt.setInt(8, newY2);

                State.setIsLoading(true);
                State.setAction("Moving selection");

                stmt.executeUpdate();
                State.setIsLoading(false);

                logSuccess(startTime, x1, y1, x2, y2, newX1, newY1, newX2, newY2);
            }
        } catch (SQLException e) {
            handleError(e, x1, y1, x2, y2, newX1, newY1, newX2, newY2);
            throw new DatabaseException("Move operation failed", e);
        }
    }

    private static void logSuccess(long startTime,
                                   int origX1, int origY1, int origX2, int origY2,
                                   int newX1, int newY1, int newX2, int newY2) {
        long durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        System.out.printf("Moved area (%d,%d)-(%d,%d) to (%d,%d)-(%d,%d) in %dms%n",
                origX1, origY1, origX2, origY2,
                newX1, newY1, newX2, newY2,
                durationMs);
    }

    private static void handleError(SQLException e,
                                    int origX1, int origY1, int origX2, int origY2,
                                    int newX1, int newY1, int newX2, int newY2) {
        System.err.printf("Failed to move area (%d,%d)-(%d,%d) to (%d,%d)-(%d,%d): %s%n",
                origX1, origY1, origX2, origY2,
                newX1, newY1, newX2, newY2,
                e.getMessage());
    }

    public static class DatabaseException extends RuntimeException {
        public DatabaseException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
