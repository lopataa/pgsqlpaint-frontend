package panels.editor;

import common.Point;
import database.state.CurrentTool;
import database.functions.DrawLine;
import panels.EditorPanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;

public class DrawMouseListener extends MouseAdapter {
    private Connection dbConn;
    private EditorPanel editorPanel;
    private final ArrayList<Point> points = new ArrayList<>();

    public DrawMouseListener(EditorPanel editorPanel) {
        this.editorPanel = editorPanel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("Mouse pressed");
            super.mousePressed(e);

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        if (CurrentTool.getCurrentTool().equals("draw")) {
            Point currentPoint = CoordinateTransformer.transformCoordinates(e, editorPanel);

            points.add(currentPoint);
        }


    }

    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        if (CurrentTool.getCurrentTool().equals("draw") && !points.isEmpty()) {
            // Use iterator for safe removal
            Iterator<Point> iterator = points.iterator();
            Point previousPoint = iterator.next();

            while (iterator.hasNext()) {
                Point currentPoint = iterator.next();
                try {
                    DrawLine.drawLine(
                            previousPoint.getX(),
                            previousPoint.getY(),
                            currentPoint.getX(),
                            currentPoint.getY()
                    );
                } catch (Exception a) {
                    continue;
                }
                previousPoint = currentPoint;
                iterator.remove();  // Safely remove processed points
            }

            // Clear any remaining points
            points.clear();
        }
    }

}
