import panels.EditorPanel;
import panels.StatusPanel;
import panels.ToolbarPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private EditorPanel editorPanel;
    private ToolbarPanel toolbarPanel;
    private StatusPanel statusPanel;

    public MainFrame() {
        initFrameProperties();
        initComponents();
        layoutComponents();
        // registerListeners();
    }

    private void initFrameProperties() {
        setTitle("pgsqlPaint");
        setSize(1024, 768);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initComponents() {
        editorPanel = new EditorPanel();
        toolbarPanel = new ToolbarPanel();
        statusPanel = new StatusPanel();
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        add(toolbarPanel, BorderLayout.WEST);
        add(editorPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
    }
}
