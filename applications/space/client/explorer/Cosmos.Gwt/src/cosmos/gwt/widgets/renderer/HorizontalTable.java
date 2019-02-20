package cosmos.gwt.widgets.renderer;

import cosmos.gwt.widgets.parser.Element;
import cosmos.gwt.widgets.parser.ElementList;

import java.util.ArrayList;
import java.util.List;

public class HorizontalTable extends MultipleElement {

    @Override
    public void add(final ElementList column) {
        if (column.getRowNumbers().size() == 1)
            addElement(column.get(0));
        else
            addColumn(column);
    }

    private void addElement(Element element) {
        if (isEmpty())
            addSingleElement(new SingleElement(element));
        else if (element.isSpace())
            addSpace(new SingleElement(element), this);
        else
            addSingleElement(new SingleElement(element));
    }

    private void addSpace(final SingleElement element, final MultipleElement table) {
        Table previousTable = table.get(size() - 1);
        if (previousTable.isMultiple())
            concatToTable(element, (MultipleElement) previousTable);
        else if (element.isAtRow(table.getRow()))
            concatElements(element, (SingleElement) previousTable, table);
        else
            addSingleElement(element);
    }

    private void concatToTable(SingleElement element, MultipleElement previousTable) {
        final Table child = previousTable.getLastElementAtRow(element.getRow());
        if (child == null) return;
        if (child.isMultiple())
            addSpace(element, (MultipleElement) child);
        else
            concatElements(element, (SingleElement) child, previousTable);
    }

    private void concatElements(final SingleElement element, final SingleElement previousElement, MultipleElement parent) {
        if (!parent.contains(previousElement))
            searchInChildren(element, previousElement, parent);
        else
            concat(element, previousElement, parent, parent.indexOf(previousElement));
    }

    private void searchInChildren(SingleElement element, SingleElement previousElement, MultipleElement parent) {
        for (Table table : parent) {
            if (table.isMultiple())
                concatElements(element, previousElement, (MultipleElement) table);
        }
    }

    private void concat(SingleElement element, SingleElement previousElement, MultipleElement parent, int index) {
        parent.remove(previousElement);
        final HorizontalTable newTable = new HorizontalTable();
        newTable.add(previousElement);
        newTable.add(element);
        parent.add(index, newTable);
        addParents(element, previousElement, parent, newTable);
    }

    private void addParents(SingleElement element, SingleElement previousElement, MultipleElement parent, HorizontalTable newTable) {
        previousElement.setParent(newTable);
        element.setParent(newTable);
        newTable.setParent(parent);
    }

    private void addSingleElement(SingleElement element) {
        if (lastColumnHasSpaceInRow(element.getElement().getRow()))
            concatToSpace(element);
        else {
            element.setParent(this);
            add(element);
        }
    }

    private boolean lastColumnHasSpaceInRow(int row) {
        if (isEmpty()) return false;
        if (get(size() - 1).isMultiple())
            return ((MultipleElement) get(size() - 1)).getLastElementAtRow(row).getElement().isSpace();
        return ((SingleElement) get(size() - 1)).getElement().isSpace();
    }

    private void concatToSpace(SingleElement element) {
        Table lastColumn = get(size() - 1);
        SingleElement space;
        if (lastColumn.isMultiple())
            space = ((MultipleElement) lastColumn).getLastElementAtRow(element.getRow());
        else
            space = (SingleElement) lastColumn;
        concatElements(element, space, (MultipleElement) space.getParent());
        if (space.getHeight() < element.getHeight())
            resizeSpace(get(size() - 1), space, element.getHeight());
    }

    private void resizeSpace(Table table, SingleElement space, int expectedHeight) {
        if (!table.isMultiple()) return;
        List<Table> spacesUsed = new ArrayList<>();
        for (Table element : (MultipleElement) table) {
            if (space == element) continue;
            if (!element.isMultiple() && ((SingleElement) element).getElement().isSpace()) {
                spacesUsed.add(element);
                space.increaseHeight(element.getHeight());
            }
            if (space.getHeight() == expectedHeight) break;
        }
        for (Table spaceUsed : spacesUsed) {
            ((MultipleElement) table).remove(spaceUsed);
        }
    }

    private void addColumn(ElementList column) {
        VerticalTable newColumn = new VerticalTable();
        for (Integer row : column.getRowNumbers())
            addElementsInRow(column.getElementsInRow(row), newColumn);
        if (newColumn.isEmpty()) return;
        add(newColumn);
        newColumn.setParent(this);
    }

    private void addElementsInRow(ElementList elements, VerticalTable newColumn) {
        if (isValidSpace(elements))
            add(elements);
        else
            newColumn.add(elements);
    }

    private boolean isValidSpace(ElementList elements) {
        return isOneSpace(elements) && getLastElementAtRow(elements.getRowNumbers().get(0)) != null && getLastElementAtRow(elements.getRowNumbers().get(0)).getHeight() == elements.get(0).getHeight();
    }

    private boolean isOneSpace(ElementList elements) {
        return elements.size() == 1 && elements.get(0).isSpace();
    }

    @Override
    public boolean isVertical() {
        return false;
    }

    @Override
    public float getTotalWidth() {
        float width = 0.0f;
        for (Table table : this)
            width += (table.isMultiple() ? ((MultipleElement)table).getTotalWidth() : table.getWidth());
        return width;
    }
}
