package database.state;

public class CurrentStroke {
    private static int stroke = 1;

    public static int getStroke() {
        return stroke;
    }

    public static void setStroke(int stroke) {
        CurrentStroke.stroke = stroke;
    }
}
