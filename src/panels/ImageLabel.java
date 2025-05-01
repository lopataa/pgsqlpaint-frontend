package panels;

import javax.swing.*;
import java.awt.*;

public class ImageLabel extends JLabel {
    private Rectangle selectionRect = null;
    private EditorPanel editorPanel;

    public ImageLabel(EditorPanel editorPanel) {
        super();
        this.editorPanel = editorPanel;
        setOpaque(false);
    }

    public void setSelectionRect(Rectangle rect) {
        this.selectionRect = rect;
        if (selectionRect != null) {
            this.selectionRect.setLocation((int) (rect.x + this.editorPanel.getImagePosition().getX()), (int) (rect.y + this.editorPanel.getImagePosition().getY()));
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (selectionRect != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.BLACK);
            float[] dash = {5f, 5f};
            g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, dash, 0));
            g2.draw(selectionRect);
        }
    }
}
