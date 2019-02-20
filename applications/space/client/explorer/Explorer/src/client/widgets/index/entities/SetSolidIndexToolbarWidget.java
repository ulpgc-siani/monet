package client.widgets.index.entities;

import client.core.model.Filter;
import client.core.model.List;
import client.core.model.NodeIndexEntry;
import client.core.model.definition.views.ViewDefinition;
import client.presenters.displays.IndexDisplay;
import client.presenters.displays.SetIndexDisplay;
import client.presenters.displays.view.ViewDisplay;
import client.services.TranslatorService;
import client.widgets.index.IndexToolbarWidget;
import client.widgets.index.IndexWidget;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

public class SetSolidIndexToolbarWidget extends IndexToolbarWidget<SetIndexDisplay, NodeIndexEntry> {
	private HTMLPanel pageControllerPanel;
	private ListBox pageController;

	public SetSolidIndexToolbarWidget(SetIndexDisplay display, TranslatorService translator) {
		super(display, translator);
	}

	@Override
	protected void createComponents() {
		super.createComponents();

		pageControllerPanel = new HTMLPanel("");
		pageControllerPanel.addStyleName(StyleName.ITEM);
		createPageLabel();
		createPageComponent();
		toolbar.add(pageControllerPanel);
		refreshPageControllerPanel(display.getPagesCount());
	}

	@Override
	protected void hook() {
		super.hook();
		display.addHook(new SetIndexDisplay.Hook() {
			@Override
			public void clear() {
			}

			@Override
			public void loadingPage() {
			}

			@Override
			public void page(IndexDisplay.Page<NodeIndexEntry> page) {
			}

			@Override
			public void pagesCount(int count) {
				refreshPageControllerPanel(count);
			}

			@Override
			public void pageEntryAdded(NodeIndexEntry entry) {
				refreshPageControllerPanel(display.getPagesCount());
			}

			@Override
			public void pageEntryDeleted() {
				refreshPageControllerPanel(display.getPagesCount());
			}

			@Override
			public void pageFailure() {
			}

			@Override
			public void pageEntryUpdated(NodeIndexEntry entry) {
			}

			@Override
			public void entityView(ViewDisplay entityViewDisplay) {
			}

			@Override
			public void selectEntries(List<NodeIndexEntry> entries) {
			}

			@Override
			public void selectOptions(Filter filter) {
			}

			@Override
			public void loadingOptions() {
			}

			@Override
			public void options(Filter filter, List<Filter.Option> options) {
			}
		});
	}

	private void createPageLabel() {
		Label label = new Label(translator.translate(TranslatorService.Label.SHOW_LIST_ITEMS));
		label.addStyleName(StyleName.PAGE_LABEL);
		pageControllerPanel.add(label);
	}

	private void createPageComponent() {

		pageController = new ListBox();
		pageController.addStyleName(StyleName.PAGE_CONTROLLER);
		pageController.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				display.setPageSize(getPageSize());
				display.clear();
				display.reloadPage(0, display.getPageSize());
			}
		});

		pageControllerPanel.add(pageController);
	}

	private void refreshPageControllerPanel(int pagesCount) {
		pageControllerPanel.setVisible(pagesCount > 1);
		addPageComponentItems(pagesCount);
	}

	private void addPageComponentItems(int pagesCount) {
		pageController.clear();

		if (pagesCount > 1)
			pageController.addItem(String.valueOf(display.getPageSize()));

		if (pagesCount > 2)
			pageController.addItem(String.valueOf(display.getPageSize() * 2));

		if (pagesCount > 5)
			pageController.addItem(String.valueOf(display.getPageSize() * 5));

		pageController.addItem(translator.translate(TranslatorService.ListLabel.ALL));
	}

	private int getPageSize() {
		int selectedIndex = pageController.getSelectedIndex();

		if (pageController.getValue(selectedIndex).equals(translator.translate(TranslatorService.ListLabel.ALL)))
			return display.getPagesCount() * display.getPageSize() + 1;

		return Integer.valueOf(pageController.getValue(selectedIndex));
	}

	public interface StyleName extends IndexWidget.StyleName {
		String PAGE_CONTROLLER = "page-controller";
		String PAGE_LABEL = "page-label";
		String ITEM = "item";
	}

	public static class Builder extends IndexToolbarWidget.Builder {

		public static void register() {
			registerBuilder(SetIndexDisplay.TYPE.toString() + IndexToolbarWidget.Builder.DESIGN + ViewDefinition.Design.DOCUMENT.toString(), new Builder());
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			return new SetSolidIndexToolbarWidget((SetIndexDisplay) presenter, translator);
		}
	}
}
