package panels.editor;

import common.Point;
import database.functions.Fill;
import database.state.CurrentColor;
import database.state.CurrentTool;
import panels.EditorPanel;

import java.awt.event.MouseEvent;

public class FillMouseListener extends ToolMouseListener {

    public FillMouseListener(EditorPanel editorPanel) {
        super(editorPanel);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!CurrentTool.getCurrentTool().equals("fill")) {
            return;
        }
        Point point = CoordinateTransformer.transformCoordinates(e, editorPanel);

        Fill.fill(point.getX(), point.getY(), CurrentColor.getRed(), CurrentColor.getGreen(), CurrentColor.getBlue());
    }
}
