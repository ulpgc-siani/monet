package client.widgets.toolbox;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.ComplexPanel;

public class ListItemWidget extends ComplexPanel {

    public ListItemWidget() {
        setElement(Document.get().createLIElement());
    }

    public ListItemWidget(String label) {
        this();
        getElement().setInnerHTML(label);
    }
}
