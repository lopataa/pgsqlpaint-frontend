package panels.editor;

import common.Point;
import panels.EditorPanel;

import java.awt.event.MouseEvent;

public class CoordinateTransformer {
    public static Point transformCoordinates(MouseEvent e, EditorPanel editorPanel) {
        java.awt.Point imagePos = editorPanel.getImagePosition();
        double scale = editorPanel.getImageScale();
        int x = (int) ((e.getX() - imagePos.getX()) / scale);
        int y = (int) ((e.getY() - imagePos.getY()) / scale);
        return new Point(x, y);
    }
}
