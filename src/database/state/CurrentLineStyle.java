package database.state;

public class CurrentLineStyle {
    private static LineStyle lineStyle = LineStyle.SOLID;

    public static LineStyle getStroke() {
        return lineStyle;
    }

    public static void setStroke(LineStyle lineStyle) {
        CurrentLineStyle.lineStyle = lineStyle;
    }

    public static String asString() {
        return switch (CurrentLineStyle.lineStyle) {
            case DASHED -> "Dashed";
            case DOTTED -> "Dotted";
            default -> "Solid";
        };
    }
}
