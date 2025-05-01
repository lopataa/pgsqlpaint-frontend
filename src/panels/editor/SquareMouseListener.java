package panels.editor;

import common.Point;
import database.functions.DrawRect;
import database.functions.DrawSquare;
import database.state.CurrentTool;
import panels.EditorPanel;

import java.awt.event.MouseEvent;

public class SquareMouseListener extends ToolMouseListener {
    private Point startPoint = null;

    public SquareMouseListener(EditorPanel editorPanel) {
        super(editorPanel);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startPoint = CoordinateTransformer.transformCoordinates(e, editorPanel);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!CurrentTool.getCurrentTool().equals("square")) {
            return;
        }
        Point endPoint = CoordinateTransformer.transformCoordinates(e, editorPanel);
        if (startPoint != null && endPoint != null) {
            DrawSquare.drawSquare(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
        }
    }
}
