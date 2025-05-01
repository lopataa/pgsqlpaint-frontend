package database;

public class State {
    public static boolean isLoading = false;
    public static String action = "";
    public static int finishedIn = -1;
    public static int lastRedrawDuration = -1;

    public static boolean getIsLoading() {
        return isLoading;
    }

    public static void setIsLoading(boolean isLoading) {
        State.isLoading = isLoading;
    }

    public static String getAction() {
        return action;
    }

    public static void setAction(String action) {
        State.action = action;
    }

    public static int getFinishedIn() {
        return finishedIn;
    }

    public static void setFinishedIn(int finishedIn) {
        State.finishedIn = finishedIn;
    }

    public static int getLastRedrawDuration() {
        return lastRedrawDuration;
    }
    public static void setLastRedrawDuration(int lastRedrawDuration) {
        State.lastRedrawDuration = lastRedrawDuration;
    }
}
