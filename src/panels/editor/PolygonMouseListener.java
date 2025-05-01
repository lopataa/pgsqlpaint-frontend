package panels.editor;

import common.Point;
import database.functions.DrawLine;
import database.state.CurrentTool;
import panels.EditorPanel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class PolygonMouseListener extends ToolMouseListener {
    private final List<Point> points = new ArrayList<>();
    private static final int CLOSURE_THRESHOLD = 5; // pixels

    public PolygonMouseListener(EditorPanel editorPanel) {
        super(editorPanel);
        setupKeyListener(editorPanel);
    }

    private void setupKeyListener(EditorPanel panel) {
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !points.isEmpty()) {
                    completePolygon();
                }
            }
        });
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!CurrentTool.getCurrentTool().equals("polygon")) return;

        Point newPoint = CoordinateTransformer.transformCoordinates(e, editorPanel);

        if (!points.isEmpty() && isNearStart(newPoint)) {
            completePolygon();
        } else {
            addPoint(newPoint);
        }
    }

    private void addPoint(Point point) {
        if (!points.isEmpty()) {
            Point prev = points.get(points.size() - 1);
            DrawLine.drawLine(prev.getX(), prev.getY(), point.getX(), point.getY());
        }
        points.add(point);
    }

    private boolean isNearStart(Point point) {
        Point first = points.get(0);
        return Math.abs(first.getX() - point.getX()) <= CLOSURE_THRESHOLD &&
                Math.abs(first.getY() - point.getY()) <= CLOSURE_THRESHOLD;
    }

    private void completePolygon() {
        if (points.size() > 2) {
            Point first = points.get(0);
            Point last = points.get(points.size() - 1);
            DrawLine.drawLine(last.getX(), last.getY(), first.getX(), first.getY());
        }
        points.clear();
    }
}
