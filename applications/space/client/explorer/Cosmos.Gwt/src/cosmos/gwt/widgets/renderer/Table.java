package cosmos.gwt.widgets.renderer;

import cosmos.gwt.widgets.parser.ElementList;

public interface Table {

    float getWidth();
    int getHeight();
    int getRow();

    boolean isMultiple();
    boolean isAtRow(int row);
    boolean isSpace();

    void add(ElementList elements);

}
