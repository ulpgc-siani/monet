package cosmos.gwt.widgets.renderer;

import cosmos.gwt.widgets.parser.Element;
import cosmos.gwt.widgets.parser.ElementList;

public class VerticalTable extends MultipleElement {

    @Override
    public void add(final ElementList row) {
        if (row.getColumnNumbers().size() == 1)
            addElement(row.get(0));
        else
            addRow(row);
    }

    private void addElement(Element element) {
        addSingleElement(new SingleElement(element));
    }

    private void addSingleElement(SingleElement singleElement) {
        singleElement.setParent(this);
        add(singleElement);
    }

    private void addRow(ElementList row) {
        HorizontalTable newRow = new HorizontalTable();
        for (Integer column : row.getColumnNumbers())
            newRow.add(row.getElementsInColumn(column));
        add(newRow);
        newRow.setParent(this);
    }

    @Override
    public boolean isVertical() {
        return true;
    }

    @Override
    public float getTotalWidth() {
        return get(0).isMultiple() ? ((MultipleElement) get(0)).getTotalWidth() : get(0).getWidth();
    }
}
