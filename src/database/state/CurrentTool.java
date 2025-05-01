package database.state;

public class CurrentTool {
    static String currentTool = "line";

    public static String getCurrentTool() {
        return currentTool;
    }

    public static void setCurrentTool(String currentTool) {
        CurrentTool.currentTool = currentTool;
    }
}
