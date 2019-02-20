package client.widgets.index.facets;

import client.core.model.definition.views.ViewDefinition;
import client.core.model.IndexEntry;
import client.presenters.displays.view.ViewDisplay;
import client.widgets.index.IndexWidget;
import client.widgets.index.IndexFacet;
import client.widgets.index.entities.SetIndexWidget;

public class SolidIndexFacet<E extends IndexEntry> implements IndexFacet<E> {
	private IndexFacet.Widget widget;

	public SolidIndexFacet(IndexFacet.Widget widget) {
		this.widget = widget;
	}

	@Override
	public int getPageSize() {
		return widget.getDisplay().getPageSize();
	}

	@Override
	public void firstPage() {
		widget.getDisplay().firstPage();
	}

	@Override
	public void nextPage() {
		widget.getDisplay().nextPage();
	}

	@Override
	public void reloadPage(boolean force) {
		widget.getDisplay().reloadPage(0, getPageSize());
	}

	@Override
	public void refreshListHeight() {
	}

	@Override
	public void refreshEntityView(E entry, ViewDisplay entityViewDisplay) {
		String design = ViewDefinition.Design.DOCUMENT.toString();
		com.google.gwt.user.client.ui.Widget widget = IndexWidget.Builder.getViewBuilder().build(entityViewDisplay, design, design);
		SetIndexWidget.NodeIndexListItem indexEntryItem = (SetIndexWidget.NodeIndexListItem)this.widget.getList().getItem(entry);
		indexEntryItem.setEntity(widget);
	}

	@Override
	public void activateIndexEntry(E entry) {
		SetIndexWidget.NodeIndexListItem indexEntryItem = (SetIndexWidget.NodeIndexListItem)widget.getList().getItem(entry);

		if (indexEntryItem.existsEntity())
			indexEntryItem.toggleEntity();
		else
			widget.getDisplay().activate(entry);
	}

}
