package database.functions;

import database.DatabaseManager;
import database.State;
import database.state.CurrentColor;
import database.state.CurrentLineStyle;
import database.state.CurrentStroke;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class DrawSquare {
    private static final String DRAW_RECT_SQL =
            "CALL draw_rect(?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public static void drawSquare(int x1, int y1, int x2, int y2) {
        // Calculate the side length based on the smallest delta
        int dx = x2 - x1;
        int dy = y2 - y1;
        int side = Math.min(Math.abs(dx), Math.abs(dy));

        // Determine the direction to preserve drag direction
        int xSign = Integer.signum(dx);
        int ySign = Integer.signum(dy);

        int x2Square = x1 + side * xSign;
        int y2Square = y1 + side * ySign;

        long startTime = System.nanoTime();

        try (Connection connection = DatabaseManager.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(DRAW_RECT_SQL)) {

                stmt.setInt(1, x1);
                stmt.setInt(2, y1);
                stmt.setInt(3, x2Square);
                stmt.setInt(4, y2Square);
                stmt.setInt(5, CurrentColor.getRed());
                stmt.setInt(6, CurrentColor.getGreen());
                stmt.setInt(7, CurrentColor.getBlue());
                stmt.setInt(8, CurrentStroke.getStroke());
                stmt.setString(9, CurrentLineStyle.asString());

                State.setIsLoading(true);
                State.setAction("Drawing square");

                stmt.executeUpdate();
                State.setIsLoading(false);

                logSuccess(startTime, x1, y1, x2Square, y2Square);
            }
        } catch (SQLException e) {
            handleError(e, x1, y1, x2Square, y2Square);
            throw new DatabaseException("Draw square failed", e);
        }
    }

    private static void logSuccess(long startTime, int x1, int y1, int x2, int y2) {
        long durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        System.out.printf("Drew square from (%d,%d) to (%d,%d) in %dms%n",
                x1, y1, x2, y2, durationMs);
    }

    private static void handleError(SQLException e, int x1, int y1, int x2, int y2) {
        System.err.printf("Failed to draw square (%d,%d)-(%d,%d): %s%n",
                x1, y1, x2, y2, e.getMessage());
    }

    public static class DatabaseException extends RuntimeException {
        public DatabaseException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
