package database.functions;

import database.DatabaseManager;
import database.State;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClearCanvas {
    private static final String CLEAR_CANVAS_SQL =
            "CALL clear_canvas(?, ?)"; // Use SELECT if it's a function

    public static void clearCanvas(int width, int height) {
        long startTime = System.nanoTime();

        try (Connection connection = DatabaseManager.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(CLEAR_CANVAS_SQL)) {

                State.setAction("Creating new canvas");
                State.setIsLoading(true);

                // Set parameters
                stmt.setInt(1, width);
                stmt.setInt(2, height);
                // Execute and commit
                stmt.executeUpdate();

                State.setIsLoading(false);
            }
        } catch (SQLException e) {
            //..
        }
    }

}
