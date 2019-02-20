package client.widgets.toolbox;

import client.core.model.List;
import client.core.system.MonetList;
import client.services.TranslatorService;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import cosmos.gwt.model.DropAble;
import cosmos.gwt.model.Scrollable;
import cosmos.gwt.utils.ImageUtils;

import static com.google.gwt.dom.client.Style.*;
import static com.google.gwt.query.client.GQuery.$;

public class HTMLListWidget<T> extends HTMLPanel implements InsertPanel, DropAble, Scrollable {
	private Mode mode;
	private final TranslatorService translator;
	protected final ListItem.Builder itemBuilder;
	private ScrollPanel scrollPanel;
	protected UnorderedListWidget items;
	private Label emptyMessage;
	private HTMLPanel toolbar;
    private List<AddHandler<T>> addHandlers = new MonetList<>();
	private List<ChangeHandler<T>> changeHandlers = new MonetList<>();
	private List<ClickHandler<T>> clickHandlers = new MonetList<>();
	private boolean emptyMessageEnabled = true;

	public enum Mode {
		SIMPLE, LIST, LIST_ENUMERATE, ICON;

		@Override
		public String toString() {
			return super.toString().toLowerCase().replace("_", "-");
		}
	}

	public HTMLListWidget(ListItem.Builder itemBuilder, TranslatorService translator) {
		this(itemBuilder, translator, null);
	}

	public HTMLListWidget(ListItem.Builder itemBuilder, TranslatorService translator, String emptyMessageText) {
		this(itemBuilder, translator, emptyMessageText, Mode.LIST);
	}

	public HTMLListWidget(ListItem.Builder itemBuilder, TranslatorService translator, String emptyMessageText, Mode mode) {
		super("");
		this.itemBuilder = itemBuilder;
		this.translator = translator;
		this.mode = mode;
		init(emptyMessageText);
		refresh();
	}

	public ListItem<T> addItem(T item) {
		return addItem(items.getWidgetCount(), item);
	}

	public ListItem<T> addItem(int index, T item) {
		ListItem<T> listItem = createListItem(item, index);
		items.insert(listItem, index);
		notifyAdd(listItem);
		refresh();
		return listItem;
	}

	public void removeItem(ListItem<T> item) {
		item.removeFromParent();
		refresh();
	}

	public void clear() {
		items.clear();
		refresh();
	}

	public WidgetCollection getItems() {
		return items.getAll();
	}

	public Widget getItem(T item) {

		for (Widget listItem : items.getAll()) {
			if (!(listItem instanceof ListItem)) continue;
			if (((ListItem)listItem).getValue() == item)
				return listItem;
		}

		return null;
	}

	public void addAddHandler(AddHandler<T> handler) {
		addHandlers.add(handler);
	}

	public void addChangeHandler(ChangeHandler<T> handler) {
		changeHandlers.add(handler);
	}

	public void addClickHandler(ClickHandler<T> handler) {
		clickHandlers.add(handler);
	}

	@Override
	public void insert(Widget widget, int beforeIndex) {
		items.insert(widget, beforeIndex);
	}

	public void enableEmptyMessage() {
		this.emptyMessageEnabled = true;
		refresh();
	}

	public void disableEmptyMessage() {
		this.emptyMessageEnabled = false;
		refresh();
	}

	@Override
    public InsertPanel getDropContainer() {
        return items;
    }

    @Override
    public ScrollPanel getScrollPanel() {
        return scrollPanel;
    }

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
		refreshMode();
		notifyModeChange();
	}

	public static abstract class ListItem<T> extends ListItemWidget implements HasValue<T> {
		private ComplexPanel controlOperationsPanel;
		private SimplePanel componentPanel;
		private ComplexPanel operationsPanel;
		private List<ValueChangeHandler<T>> valueChangeHandlers = new MonetList<>();
		protected List<ClickHandler<T>> clickHandlers = new MonetList<>();
		protected T value;
		private Grid grid;

		public ListItem(T value) {
			super();
			this.value = value;
		}

		protected void init() {
			createLayout();
			build();
		}

		protected abstract Widget[] createControlOperations();
		protected abstract Widget createComponent();
		protected abstract Widget[] createOperations();

		public void addControlOperation(Widget operation) {
			controlOperationsPanel.add(operation);
			enableControlOperationsPanel();
		}

		public void addOperation(Widget operation) {
			operation.addStyleName(StyleName.OPERATION);
			operationsPanel.add(operation);
			enableOperationsPanel();
		}

		@Override
		public T getValue() {
			return value;
		}

		@Override
		public void setValue(T value) {
			this.value = value;
		}

		@Override
		public void setValue(T value, boolean fireEvents) {
			this.value = value;
		}

		public void refreshValue(T value) {
			setValue(value);
			componentPanel.setWidget(createComponent());
		}

		@Override
		public HandlerRegistration addValueChangeHandler(com.google.gwt.event.logical.shared.ValueChangeHandler<T> handler) {
			valueChangeHandlers.add((ValueChangeHandler<T>)handler);
			return null;
		}

		public HandlerRegistration addClickHandler(ClickHandler<T> handler) {
			clickHandlers.add(handler);
			return null;
		}

		public interface ValueChangeHandler<T> extends com.google.gwt.event.logical.shared.ValueChangeHandler<T> {
			void onChange(ListItem<T> item);
		}

		public interface ClickHandler<T> {
			void onClick(ListItem<T> item);
		}

		public static abstract class Builder<T> {

			public Builder() {
			}

			public abstract ListItem<T> build(T value, Mode mode);

			public Mode[] getAcceptedModes() {
				return new Mode[0];
			}
		}

		protected void notifyChange(T newValue) {
			this.setValue(newValue);
			for (ValueChangeHandler<T> valueChangeHandler : valueChangeHandlers)
				valueChangeHandler.onChange(this);
		}

		protected void notifyClick() {
			for (ClickHandler<T> clickHandler : clickHandlers)
				clickHandler.onClick(this);
		}

		protected void notifyClick(ListItem<T> item) {
			for (ClickHandler<T> clickHandler : clickHandlers)
				clickHandler.onClick(item);
		}

		private void enableControlOperationsPanel() {
			grid.getWidget(0, 0).getElement().getParentElement().getStyle().setDisplay(Display.BLOCK);
		}

		private void disableControlOperationsPanel() {
			grid.getWidget(0, 0).getElement().getParentElement().getStyle().setDisplay(Display.NONE);
		}

		private void enableOperationsPanel() {
			grid.getWidget(0, 2).getElement().getParentElement().getStyle().setDisplay(Display.BLOCK);
		}

		private void disableOperationsPanel() {
			grid.getWidget(0, 2).getElement().getParentElement().getStyle().setDisplay(Display.NONE);
		}

	    private void updateContentPanelWidth() {
		    grid.getWidget(0, 1).getElement().getParentElement().getStyle().setWidth(100, Unit.PCT);
	    }

		private void createLayout() {

			grid = new Grid(1, 3);
			grid.addStyleName(StyleName.LAYOUT);
			grid.setWidth("100%");
			grid.setWidget(0, 0, createControlOperationsPanel());
			grid.setWidget(0, 1, createContentPanel());
			grid.setWidget(0, 2, createOperationsPanel());

			updateContentPanelWidth();
			disableControlOperationsPanel();
			disableOperationsPanel();

			add(grid, $(getElement()).get(0));
		}

		private Widget createControlOperationsPanel() {
			controlOperationsPanel = new HorizontalPanel();
			controlOperationsPanel.setStyleName(StyleName.CONTROL_OPERATIONS_PANEL);
			return controlOperationsPanel;
		}

		private Widget createContentPanel() {
			componentPanel = new SimplePanel();
			componentPanel.setStyleName(StyleName.CONTENT_PANEL);
			return componentPanel;
		}

		private Widget createOperationsPanel() {
			operationsPanel = new HorizontalPanel();
			operationsPanel.setStyleName(StyleName.OPERATIONS_PANEL);
			return operationsPanel;
		}

		private void build() {
			buildControlOperations();
			componentPanel.add(createComponent());
			buildOperations();
		}

		private void buildControlOperations() {
			Widget[] controlOperations = createControlOperations();
			if (controlOperations.length > 0)
				enableControlOperationsPanel();
			for (Widget controlOperation : controlOperations)
				controlOperationsPanel.add(controlOperation);
		}

		private void buildOperations() {
			Widget[] operations = createOperations();
			if (operations.length > 0)
				enableOperationsPanel();
			for (Widget operation : operations)
				operationsPanel.add(operation);
		}

		protected interface StyleName {
			String LAYOUT = "layout";
			String CONTROL_OPERATIONS_PANEL = "control-operations-panel";
			String CONTENT_PANEL = "content-panel";
			String OPERATIONS_PANEL = "operations-panel";
			String OPERATION = "operation";

			String INPUT = "input";
			String FOCUSABLE = "focusable";
			String DELETE = "delete";
			String SPACE = "space";
		}

	}

	public static class ReadOnlyListItem<T> extends ListItem<T> {

		public ReadOnlyListItem(T value) {
			super(value);
			init();
		}

		@Override
		protected Widget[] createControlOperations() {
			return new Widget[0];
		}

		@Override
		protected Widget createComponent() {
			return new InlineLabel(value.toString());
		}

		@Override
		protected Widget[] createOperations() {
			return new Widget[0];
		}
		public static class Builder<T> extends HTMLListWidget.ListItem.Builder<T> {
			protected final TranslatorService translator;

			public Builder(TranslatorService translator) {
				super();
				this.translator = translator;
			}

			@Override
			public HTMLListWidget.ListItem<T> build(T value, Mode mode) {
				return new ReadOnlyListItem<>(value);
			}
		}
	}

	public interface AddHandler<T> {
		void onAdd(ListItem<T> item);
	}

	public interface ChangeHandler<T> {
		void onChangeItem(int index, T value);
		void onChangeMode(Mode mode);
	}

	public interface ClickHandler<T> {
		void onClick(int index, T value);
	}

	protected void addListItemBehaviors(final ListItem<T> listItem, final int index) {
		listItem.addValueChangeHandler(new ListItem.ValueChangeHandler<T>() {
            @Override
            public void onChange(ListItem<T> item) {
                notifyItemChange(item, index);
            }

            @Override
            public void onValueChange(ValueChangeEvent<T> event) {
            }
        });
		listItem.addClickHandler(new ListItem.ClickHandler<T>() {
            @Override
            public void onClick(ListItem<T> item) {
                notifyClick(item, index);
            }
        });
	}

	private void init(String emptyMessageText) {
		addStyleName(StyleName.HTML_LIST);

		initToolbar();
		initEmptyMessage(emptyMessageText);
		initItems();
	}

	private void initToolbar() {
        HorizontalPanel toolbarItems = new HorizontalPanel();
		toolbarItems.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		toolbar = new HTMLPanel("");
		toolbar.addStyleName(StyleName.TOOLBAR);
		toolbar.add(toolbarItems);
		hideToolbar();

		addModeItems(toolbarItems);

		add(toolbar);
	}

	private void initEmptyMessage(String emptyMessageText) {
		emptyMessage = new Label();
		emptyMessage.setText(emptyMessageText != null ? emptyMessageText : translator.translate(TranslatorService.ListLabel.MESSAGE_WHEN_EMPTY));
		$(emptyMessage).addClass(StyleName.MESSAGE, StyleName.EMPTY_MESSAGE);
		add(emptyMessage);
		emptyMessage.setVisible(false);
	}

	private void addModeItems(HorizontalPanel options) {

		if (itemBuilder.getAcceptedModes().length <= 1)
			return;

		showToolbar();

		for (Mode mode : itemBuilder.getAcceptedModes())
			options.add(createModeOption(mode));
	}

	private void initItems() {
		items = new UnorderedListWidget();
		items.addStyleName(StyleName.ITEMS);
		scrollPanel = createScrollPanel();
		add(scrollPanel);
	}

	private ScrollPanel createScrollPanel() {
		final ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.addStyleName(StyleName.SCROLL_PANEL);
		scrollPanel.addDomHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				if (scrollPanel.getOffsetHeight() <= getOffsetHeight() + 10) return;
                if (Window.Navigator.getPlatform().contains("Win"))
                    RootPanel.get().addStyleName(StyleName.NO_SCROLL_WIN);
                else
                    RootPanel.get().addStyleName(StyleName.NO_SCROLL_MAC);
			}
		}, MouseOverEvent.getType());
		scrollPanel.addDomHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (scrollPanel.getOffsetHeight() > getOffsetHeight() + 10) {
                    RootPanel.get().removeStyleName(StyleName.NO_SCROLL_WIN);
                    RootPanel.get().removeStyleName(StyleName.NO_SCROLL_MAC);
                }
			}
		}, MouseOutEvent.getType());
		scrollPanel.add(items);
		return scrollPanel;
	}

	private Anchor createModeOption(final Mode mode) {
		Anchor option = new Anchor();

		option.setHTML("<img/>");

		GQuery optionElement = $(option);
		optionElement.addClass(StyleName.MODE, mode.toString().toLowerCase());
		Element img = optionElement.find("img").get(0);
		img.setTitle(getModeLabel(mode));
		img.setAttribute("src", ImageUtils.getBlankImage());

		option.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (getMode() != mode)
					setMode(mode);
			}
		});

		return option;
	}

	private String getModeLabel(Mode mode) {
		if (mode == Mode.LIST)
			return translator.translate(TranslatorService.ListLabel.MODE_LIST);
		if (mode == Mode.ICON)
			return translator.translate(TranslatorService.ListLabel.MODE_ICON);
		return "";
	}

	private ListItem<T> createListItem(T item, int index) {
		ListItem<T> listItem = itemBuilder.build(item, mode);
		addListItemBehaviors(listItem, index);
		return listItem;
	}

	private void refresh() {
		refreshEmptyMessage();
		refreshToolbar();
		refreshItems();
	}

	private void refreshEmptyMessage() {
		emptyMessage.setVisible(emptyMessageEnabled && items.size() <= 0);
	}

	private void refreshToolbar() {
		toolbar.setVisible(itemBuilder.getAcceptedModes().length > 1 && items.size() > 0);
		refreshMode();
	}

	private void refreshItems() {
		scrollPanel.setVisible(items.size()>0);
	}

	private void refreshMode() {
		removeStyleName(Mode.LIST.toString().toLowerCase());
		removeStyleName(Mode.ICON.toString().toLowerCase());
		addStyleName(mode.toString().toLowerCase());
	}

	private void hideToolbar() {
		toolbar.setVisible(false);
	}

	private void showToolbar() {
		toolbar.setVisible(true);
	}

	private void notifyAdd(ListItem<T> item) {
		for (AddHandler<T> handler : addHandlers)
			handler.onAdd(item);
	}

	private void notifyItemChange(ListItem<T> item, int index) {
		for (ChangeHandler<T> handler : changeHandlers)
			handler.onChangeItem(index, item.getValue());
	}

	private void notifyModeChange() {
		for (ChangeHandler<T> handler : changeHandlers)
			handler.onChangeMode(getMode());
	}

	private void notifyClick(ListItem<T> item, int index) {
		for (ClickHandler<T> handler : clickHandlers)
			handler.onClick(index, item.getValue());
	}

	public interface StyleName {
		String TOOLBAR = "toolbar";
		String HTML_LIST = "html-list";
		String MODE = "mode";
		String ITEMS = "items";
		String SCROLL_PANEL = "scroll-panel";
		String MESSAGE = "msg";
		String EMPTY_MESSAGE = "empty";
        String NO_SCROLL_WIN = "no-scroll-win";
        String NO_SCROLL_MAC = "no-scroll-mac";
	}
}