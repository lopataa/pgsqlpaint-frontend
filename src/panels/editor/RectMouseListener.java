package panels.editor;

import common.Point;
import database.functions.DrawLine;
import database.functions.DrawRect;
import database.state.CurrentTool;
import panels.EditorPanel;

import java.awt.event.MouseEvent;

public class RectMouseListener extends ToolMouseListener {
    private Point startPoint = null;

    public RectMouseListener(EditorPanel editorPanel) {
        super(editorPanel);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startPoint = CoordinateTransformer.transformCoordinates(e, editorPanel);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!CurrentTool.getCurrentTool().equals("rectangle")) {
            return;
        }
        Point endPoint = CoordinateTransformer.transformCoordinates(e, editorPanel);
        if (startPoint != null && endPoint != null) {
            DrawRect.drawRect(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
        }
    }
}
