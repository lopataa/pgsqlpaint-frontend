package panels.editor;

import panels.EditorPanel;
import java.awt.event.MouseAdapter;

public abstract class ToolMouseListener extends MouseAdapter {
    protected final EditorPanel editorPanel;

    public ToolMouseListener(EditorPanel editorPanel) {
        this.editorPanel = editorPanel;
    }
}
