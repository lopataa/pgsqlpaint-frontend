package database.state;

public class CurrentRenderer {
    public static Renderer renderer = Renderer.PNG;

    public static void setColor(Renderer renderer) {
        CurrentRenderer.renderer = renderer;
    }

    public static Renderer getRenderer() {
        return renderer;
    }
}
