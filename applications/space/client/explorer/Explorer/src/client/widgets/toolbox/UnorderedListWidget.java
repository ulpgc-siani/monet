package client.widgets.toolbox;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;

import static com.google.gwt.query.client.GQuery.$;

public class UnorderedListWidget extends ComplexPanel implements InsertPanel {

	public UnorderedListWidget() {
		setElement(Document.get().createULElement());
	}

	public void setId(String id) {
		getElement().setId(id);
	}

	public void add(Widget widget) {
		super.add(widget, $(getElement()).get(0));
	}

	public WidgetCollection getAll() {
		return super.getChildren();
	}

	public int size() {
		return $(getElement()).not("li li").find("li").size();
	}

	@Override
	public void insert(Widget widget, int beforeIndex) {
		super.insert(widget, $(getElement()).get(0), beforeIndex, true);
	}

}
