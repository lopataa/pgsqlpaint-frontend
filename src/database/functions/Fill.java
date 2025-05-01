package database.functions;

import database.DatabaseManager;
import database.State;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class Fill {
    private static final String DRAW_LINE_SQL =
            "CALL fill(?, ?, ?, ?, ?)"; // Use SELECT if it's a function

    public static void fill(int x, int y, int r, int g, int b) {
        long startTime = System.nanoTime();

        try (Connection connection = DatabaseManager.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(DRAW_LINE_SQL)) {

                // Set parameters
                stmt.setInt(1, x);
                stmt.setInt(2, y);
                stmt.setInt(3, r);
                stmt.setInt(4, g);
                stmt.setInt(5, b);

                State.setIsLoading(true);
                State.setAction("Fill");

                // Execute and commit
                stmt.executeUpdate();

                State.setIsLoading(false);
                long endTime = System.nanoTime();

                System.out.println("Fill " + x + "," + y + "," + r + "," + g + "," + b);

                logSuccess(startTime);
            }
        } catch (SQLException e) {
            handleError(e);
            throw new DatabaseException("Fill failed", e);
        }
    }

    private static void logSuccess(long startTime) {
        long durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        System.out.printf("Fill in %dms%n", durationMs);
    }

    private static void handleError(SQLException e) {
        System.err.printf("Failed to fill %s%n", e.getMessage());
        // Consider adding metrics/alerting here
    }

    public static class DatabaseException extends RuntimeException {
        public DatabaseException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
