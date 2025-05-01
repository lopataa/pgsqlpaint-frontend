package panels.editor;

import common.Point;
import database.functions.MoveTool;
import database.state.CurrentTool;
import panels.EditorPanel;
import panels.ImageLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class MoveMouseListener extends ToolMouseListener {
    private Point sourceStart, sourceEnd, destStart;
    private boolean selectingSource = true;
    private static final float[] DASH = {5f, 5f};
    private static final BasicStroke DASHED_STROKE = new BasicStroke(1,
            BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, DASH, 0);

    public MoveMouseListener(EditorPanel editorPanel) {
        super(editorPanel);
    }

    private Rectangle createRectFromPoints(Point p1, Point p2) {
        int x = Math.min(p1.getX(), p2.getX());
        int y = Math.min(p1.getY(), p2.getY());
        int w = Math.abs(p1.getX() - p2.getX());
        int h = Math.abs(p1.getY() - p2.getY());
        return new Rectangle(x, y, w, h);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!CurrentTool.getCurrentTool().equals("move")) return;

        Point current = CoordinateTransformer.transformCoordinates(e, editorPanel);

        if (selectingSource) {
            sourceStart = current;
        } else {
            destStart = current;
        }
        // Show initial rectangle
        Rectangle rect = createRectFromPoints(sourceStart, sourceEnd);
        ((ImageLabel)editorPanel.imageLabel).setSelectionRect(rect);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!CurrentTool.getCurrentTool().equals("move")) return;

        Point current = CoordinateTransformer.transformCoordinates(e, editorPanel);

        if (selectingSource) {
            sourceEnd = current;
        } else {
            destStart = current; // Update destination while dragging
        }
        // Show initial rectangle
        Rectangle rect = createRectFromPoints(sourceStart, sourceEnd);
        ((ImageLabel)editorPanel.imageLabel).setSelectionRect(rect);
        editorPanel.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!CurrentTool.getCurrentTool().equals("move")) return;
        if (SwingUtilities.isRightMouseButton(e)) return; // Ignore right button releases

        Point current = CoordinateTransformer.transformCoordinates(e, editorPanel);

        if (selectingSource) {
            sourceEnd = current;
            selectingSource = false;
        } else {
            Point destEnd = current;
            performMoveOperation();
            // Do NOT reset selection here!
        }
        // Show initial rectangle
        Rectangle rect = createRectFromPoints(sourceStart, sourceEnd);
        ((ImageLabel)editorPanel.imageLabel).setSelectionRect(rect);
        editorPanel.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) { // Right-click cancel
            resetSelection();
            ((ImageLabel)editorPanel.imageLabel).setSelectionRect(null);
            editorPanel.repaint();
        }

    }

    public void drawPreview(Graphics2D g) {
        if (!CurrentTool.getCurrentTool().equals("move")) return;

        g.setColor(Color.BLACK);
        g.setStroke(DASHED_STROKE);

        if (selectingSource && sourceStart != null && sourceEnd != null) {
            drawDashedRect(g, sourceStart, sourceEnd);
        } else if (destStart != null) {
            drawDashedRect(g, destStart, destStart); // Show destination marker
        }
    }

    private void drawDashedRect(Graphics2D g, Point p1, Point p2) {
        int x = Math.min(p1.getX(), p2.getX());
        int y = Math.min(p1.getY(), p2.getY());
        int width = Math.abs(p1.getX() - p2.getX());
        int height = Math.abs(p1.getY() - p2.getY());

        g.drawRect(x, y, width, height);
    }

    private void performMoveOperation() {
        if (sourceStart == null || sourceEnd == null || destStart == null) return;

        MoveTool.moveSelection(
                sourceStart.getX(), sourceStart.getY(),
                sourceEnd.getX(), sourceEnd.getY(),
                destStart.getX(), destStart.getY(),
                destStart.getX() + (sourceEnd.getX() - sourceStart.getX()),
                destStart.getY() + (sourceEnd.getY() - sourceStart.getY())
        );
    }

    private void resetSelection() {
        sourceStart = null;
        sourceEnd = null;
        destStart = null;
        selectingSource = true;
    }
}
