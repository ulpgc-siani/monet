package client.widgets.popups.dialogs.picture.imagecropper;

public class Area {

    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public Area(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getLeft() {
        return x;
    }

    public int getTop() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getScaledWidth(double scale) {
        return (int) (width * scale);
    }

    public int getScaledHeight(double scale) {
        return (int) (height * scale);
    }
}
