package database.state;

public class CurrentColor {
    public static int r,g,b = 0;

    public static void setColor(int r, int g, int b) {
        CurrentColor.r = r;
        CurrentColor.g = g;
        CurrentColor.b = b;
    }

    public static int getRed() {
        return CurrentColor.r;
    }

    public static int getGreen() {
        return CurrentColor.g;
    }

    public static int getBlue() {
        return CurrentColor.b;
    }
}
