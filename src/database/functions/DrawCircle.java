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

public class DrawCircle {
    private static final String DRAW_CIRCLE_SQL =
            "CALL draw_circle(?, ?, ?, ?, ?, ?, ?, ?)";

    public static void drawCircle(int centerX, int centerY, int edgeX, int edgeY) {
        long startTime = System.nanoTime();

        // Calculate radius from center to edge point
        int radius = calculateRadius(centerX, centerY, edgeX, edgeY);

        try (Connection connection = DatabaseManager.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(DRAW_CIRCLE_SQL)) {

                // Set parameters
                stmt.setInt(1, centerX);
                stmt.setInt(2, centerY);
                stmt.setInt(3, radius);
                stmt.setInt(4, CurrentColor.getRed());
                stmt.setInt(5, CurrentColor.getGreen());
                stmt.setInt(6, CurrentColor.getBlue());
                stmt.setInt(7, CurrentStroke.getStroke());
                stmt.setString(8, CurrentLineStyle.asString());

                State.setIsLoading(true);
                State.setAction("Drawing circle");

                // Execute and commit
                stmt.executeUpdate();

                State.setIsLoading(false);
                logSuccess(startTime, centerX, centerY, edgeX, edgeY, radius);
            }
        } catch (SQLException e) {
            handleError(e, centerX, centerY, edgeX, edgeY);
            throw new DatabaseException("Circle drawing failed", e);
        }
    }

    private static int calculateRadius(int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        return (int) Math.round(Math.hypot(dx, dy));
    }

    private static void logSuccess(long startTime, int cx, int cy, int ex, int ey, int radius) {
        long durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        System.out.printf("Drew circle center (%d,%d) through (%d,%d) [radius %d] in %dms%n",
                cx, cy, ex, ey, radius, durationMs);
    }

    private static void handleError(SQLException e, int cx, int cy, int ex, int ey) {
        System.err.printf("Failed to draw circle (%d,%d)-(%d,%d): %s%n",
                cx, cy, ex, ey, e.getMessage());
    }

    public static class DatabaseException extends RuntimeException {
        public DatabaseException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
