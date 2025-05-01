package panels.editor;

import common.Point;
import database.state.CurrentColor;
import database.state.CurrentTool;
import database.DatabaseManager;
import panels.EditorPanel;
import panels.editor.textdialog.TextProperties;
import panels.editor.textdialog.TextToolDialog;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TextMouseListener extends ToolMouseListener {
    public TextMouseListener(EditorPanel editorPanel) {
        super(editorPanel);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        if (CurrentTool.getCurrentTool().equals("text")) {
            // ask for text
            TextProperties textProperties = TextToolDialog.collectTextProperties();

            // get x, y
            Point point = CoordinateTransformer.transformCoordinates(e, editorPanel);

            // call db function
            if (textProperties.getText().trim().isEmpty()) {
                return;
            }
            Connection connection = null;
            try {
                connection = DatabaseManager.getConnection();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            PreparedStatement statement = null;
            try {
                statement = connection.prepareStatement("CALL text(?, ?, ?, ?, ?, ?, ?, ?, ?)");
                statement.setString(1, textProperties.getText());
                statement.setInt(2, point.getX());
                statement.setInt(3, point.getY());
                statement.setString(4, textProperties.getFontType());
                statement.setString(5, textProperties.getFontStyle());
                statement.setInt(6, textProperties.getFontSize());
                statement.setInt(7, CurrentColor.getRed());
                statement.setInt(8, CurrentColor.getGreen());
                statement.setInt(9, CurrentColor.getBlue());
                statement.executeUpdate();

                connection.prepareStatement("NOTIFY repaint;").executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        }
    }
}
