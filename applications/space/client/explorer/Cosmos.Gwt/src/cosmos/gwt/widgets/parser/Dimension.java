package cosmos.gwt.widgets.parser;

public class Dimension {

    private final float width;
    private int height;

    public Dimension(float width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean equals(Object object) {
        if ((object == null) || object.getClass() != getClass())
            return false;
        Dimension dimension = (Dimension) object;
        return ((dimension.width == width) && (dimension.height == height));
    }

    public float getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void increaseHeight(int height) {
        this.height += height;
    }
}
