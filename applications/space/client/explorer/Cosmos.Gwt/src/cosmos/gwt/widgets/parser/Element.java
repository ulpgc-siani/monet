package cosmos.gwt.widgets.parser;

public class Element {

    private final String element;
    private final Dimension dimension;
    private final Position position;

    public Element(String element, Dimension dimension, Position position) {
        this.element = element;
        this.dimension = dimension;
        this.position = position;
    }

    @Override
    public boolean equals(Object object) {
        if ((object == null) || object.getClass() != getClass()) return false;
        Element element = (Element) object;
        return this.element.equals(element.element) && dimension.equals(element.dimension) && position.equals(element.position);
    }

    public char getType() {
        return element.charAt(0);
    }

    public float getWidth() {
        return dimension.getWidth();
    }

    public int getHeight() {
        return dimension.getHeight();
    }

    public int getRow() {
        return position.getRow();
    }

    public int getColumn() {
        return position.getColumn();
    }

    public boolean isSpace() {
        return getType() == ' ';
    }

    public void increaseHeight(int height) {
        dimension.increaseHeight(height);
    }
}