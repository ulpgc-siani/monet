package client.widgets.index.facets;

import client.core.model.IndexEntry;
import client.presenters.displays.IndexDisplay;
import client.presenters.displays.view.ViewDisplay;
import client.widgets.index.IndexFacet;
import client.widgets.toolbox.HTMLListWidget;
import com.google.gwt.dom.client.Element;

import static client.widgets.toolbox.ErasableListWidget.ListItem;

public class FluidIndexFacet<E extends IndexEntry> implements IndexFacet<E> {
	private final IndexFacet.Widget widget;
	private final DesktopHelper helper;

	public FluidIndexFacet(IndexFacet.Widget widget, DesktopHelper helper) {
		this.widget = widget;
		this.helper = helper;
	}

	@Override
	public int getPageSize() {
		Size listItemSize = getListItemSize();
		Size listSize = getListSize();

		if (listItemSize.height <= 0 || listItemSize.width <= 0)
			return 0;

		int numItemsPerRow = Math.round(listSize.width / listItemSize.width);

		if (numItemsPerRow == 0)
			numItemsPerRow = 1;

		return (Math.round(listSize.height/listItemSize.height) + 1) * numItemsPerRow;
	}

	@Override
	public void firstPage() {
		refreshListHeight();
		int newPageSize = getPageSize();
		widget.getDisplay().setPageSize(newPageSize);
		widget.getDisplay().firstPage();
	}

	@Override
	public void nextPage() {
		widget.getDisplay().nextPage();
	}

	@Override
	public void reloadPage(boolean force) {
		refreshListHeight();
		int newPageSize = getPageSize();
		IndexDisplay display = widget.getDisplay();
		HTMLListWidget list = widget.getList();

		if (!force && display.getPageSize() >= newPageSize)
			return;

		display.setPageSize(newPageSize);

		int listItemsCount = list.getItems().size();

		if (listItemsCount > newPageSize)
			return;

		display.reloadPage(listItemsCount + 1, newPageSize - listItemsCount);
	}

	@Override
	public void refreshEntityView(E e, ViewDisplay viewDisplay) {
	}

	@Override
	public void activateIndexEntry(E entry) {
		widget.getDisplay().activate(entry);
	}

	public void refreshListHeight() {
		widget.getList().getScrollPanel().setHeight(getListSize().height + "px");
	}

	private Size getListSize() {
		final HTMLListWidget list = widget.getList();
		final int mainPanelClientHeight = helper.getMain().getClientHeight();
		final int mainEntityPanelClientHeight = helper.getMainDisplay().getClientHeight();
		final int absoluteListTop = list.getAbsoluteTop();
		final int offsetHeight = (mainPanelClientHeight - mainEntityPanelClientHeight) + absoluteListTop;

		return new Size(list.getOffsetWidth(), (offsetHeight > mainPanelClientHeight ? 0 : mainPanelClientHeight - offsetHeight));
	}

	private Size getListItemSize() {
		ListItem<E> item;
		boolean clearList = false;
		final HTMLListWidget list = widget.getList();

		if (list.getItems().size() > 0)
			item = (ListItem<E>) list.getItems().get(0);
		else {
			item = (ListItem<E>) list.addItem(widget.createLoadingEntry());
			clearList = true;
		}

		final int itemHeight = item.getElement().getOffsetHeight();
		final int itemWidth = item.getElement().getOffsetWidth();

		if (clearList)
			list.clear();

		return new Size(itemWidth > 0 ? itemWidth : list.getOffsetWidth(), itemHeight > 0 ? itemHeight : 10);
	}

	private static class Size {
		public int width;
		public int height;

		public Size(int width, int height) {
			this.width = width;
			this.height = height;
		}
	}

	public interface DesktopHelper {
		Element getMain();
		Element getMainDisplay();
	}
}
