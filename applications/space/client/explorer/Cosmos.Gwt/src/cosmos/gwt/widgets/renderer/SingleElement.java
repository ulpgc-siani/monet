package cosmos.gwt.widgets.renderer;

import cosmos.gwt.widgets.parser.Element;
import cosmos.gwt.widgets.parser.ElementList;

public class SingleElement implements Table {

    private final Element element;
    private Table parent;

    public SingleElement(Element element) {
        this.element = element;
    }

    public Element getElement() {
        return element;
    }

    public Table getParent() {
        return parent;
    }

    @Override
    public float getWidth() {
        return element.getWidth();
    }

    @Override
    public int getHeight() {
        return element.getHeight();
    }

    @Override
    public int getRow() {
        return element.getRow();
    }

    @Override
    public boolean isMultiple() {
        return false;
    }

    @Override
    public boolean isAtRow(int row) {
        return element.getRow() == row;
    }

    @Override
    public boolean isSpace() {
        return element.isSpace();
    }

    @Override
    public void add(ElementList elements) {
    }

    public void setParent(Table parent) {
        this.parent = parent;
    }

    public void increaseHeight(int height) {
        element.increaseHeight(height);
    }
}
