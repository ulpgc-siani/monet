package cosmos.gwt.widgets.renderer;

import java.util.ArrayList;

public abstract class MultipleElement extends ArrayList<Table> implements Table {

    protected Table parent;

    public abstract boolean isVertical();

    @Override
    public boolean isMultiple() {
        return true;
    }

    @Override
    public float getWidth() {
        if (parent == null) return 1;
        float maxWidth = 0.0f;
        for (Table table : this) {
            if (table.getWidth() > maxWidth)
                maxWidth = table.getWidth();
        }
        return maxWidth;
    }

    @Override
    public int getHeight() {
        int maxHeight = 1;
        for (Table table : this) {
            if (table.getHeight() > maxHeight)
                maxHeight = table.getHeight();
        }
        return maxHeight;
    }

    @Override
    public int getRow() {
        return get(0).getRow();
    }

    @Override
    public boolean isAtRow(int row) {
        return getRow() == row;
    }

    public SingleElement getLastElementAtRow(int row) {
        SingleElement singleElement = null;
        for (Table element : this) {
            if (element.isMultiple() && element.isAtRow(row))
                singleElement = ((MultipleElement) element).getLastElementAtRow(row);
            else if (element.isAtRow(row))
                singleElement = (SingleElement) element;
        }
        return singleElement;
    }

    @Override
    public boolean isSpace() {
        for (Table element : this) {
            if (!element.isSpace()) return false;
        }
        return true;
    }

    public void setParent(Table parent) {
        this.parent = parent;
    }

    public abstract float getTotalWidth();
}
