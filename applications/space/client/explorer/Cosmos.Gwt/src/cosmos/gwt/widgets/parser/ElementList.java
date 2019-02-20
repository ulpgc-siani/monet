package cosmos.gwt.widgets.parser;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ElementList extends ArrayList<Element> {

    public List<Integer> getRowNumbers() {
        List<Integer> rows = new ArrayList<>();
        int row = get(0).getRow();
        while (row <= lastRow()) {
            if (getElementsInRow(row).size() == 0)
                row++;
            else {
                rows.add(row);
                row += getElementsInRow(row).getMaxHeight();
            }
        }
        return rows;
    }

    private int lastRow() {
        int lastRow = 0;
        for (Element element : this)
            if (element.getRow() > lastRow)
                lastRow = element.getRow();
        return lastRow;
    }

    public ElementList getElements(int row) {
        ElementList list = new ElementList();
        for (int i = row; i < (row + getElementsInRow(row).getMaxHeight()); i++)
            list.addAll(getElementsInRow(i));
        return list;
    }

    public ElementList getElementsInRow(int row) {
        ElementList list = new ElementList();
        for (Element element : this) {
            if (element.getRow() == row)
                list.add(element);
        }
        return list;
    }

    public int getMaxHeight() {
        int maxHeight = 0;
        for (Element element : this) {
            if (element.getHeight() > maxHeight)
                maxHeight = element.getHeight();
        }
        return maxHeight;
    }

    public List<Integer> getColumnNumbers() {
        Set<Integer> columns = new LinkedHashSet<>();
        for (Element element : this)
            columns.add(element.getColumn());
        return new ArrayList<>(columns);
    }

    public ElementList getElementsInColumn(Integer column) {
        ElementList list = new ElementList();
        for (Element element : this)
            if (element.getColumn() == column)
                list.add(element);
        return list;
    }
}
