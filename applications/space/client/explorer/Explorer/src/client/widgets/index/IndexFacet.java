package client.widgets.index;

import client.presenters.displays.view.ViewDisplay;
import client.widgets.toolbox.HTMLListWidget;

public interface IndexFacet<IndexEntry extends client.core.model.IndexEntry> {

	int getPageSize();
	void firstPage();
	void nextPage();
	void reloadPage(boolean force);
	void refreshListHeight();
	void refreshEntityView(IndexEntry indexIndexIndexEntry, ViewDisplay entityViewDisplay);
	void activateIndexEntry(IndexEntry indexIndexIndexEntry);

	interface Widget<IndexDisplay extends client.presenters.displays.IndexDisplay, IndexEntry extends client.core.model.IndexEntry> {
		HTMLListWidget<IndexEntry> getList();
		IndexDisplay getDisplay();
		IndexEntry createLoadingEntry();
	}

}
