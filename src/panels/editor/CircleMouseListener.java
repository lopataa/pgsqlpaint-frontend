package panels.editor;

import common.Point;
import database.functions.DrawCircle;
import database.functions.DrawLine;
import database.state.CurrentTool;
import panels.EditorPanel;

import java.awt.event.MouseEvent;

public class CircleMouseListener extends ToolMouseListener {
    private Point startPoint = null;

    public CircleMouseListener(EditorPanel editorPanel) {
        super(editorPanel);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startPoint = CoordinateTransformer.transformCoordinates(e, editorPanel);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!CurrentTool.getCurrentTool().equals("circle")) {
            return;
        }

        Point endPoint = CoordinateTransformer.transformCoordinates(e, editorPanel);
        if (startPoint != null && endPoint != null) {
            DrawCircle.drawCircle(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
        }
    }
}
