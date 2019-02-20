package client.widgets;

import client.presenters.displays.ExplorationDisplay;
import client.services.TranslatorService;
import client.widgets.toolbox.HTMLListWidget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import cosmos.presenters.Presenter;

import static client.presenters.displays.ExplorationDisplay.ExplorationItemDisplay;
import static com.google.gwt.dom.client.Style.Position;

public class ExplorationWidget extends HTMLListWidget<ExplorationItemDisplay> {

	public static final int DELAY_TAB_CHANGE = 800;
	private final ExplorationDisplay display;
	private Timer timer;

	public ExplorationWidget(final ExplorationDisplay display, final TranslatorService translator, final Mode mode) {
		super(new ExplorationItem.Builder(new ExplorationWidgetControl() {
			@Override
			public ExplorationItemDisplay getActive() {
				return display.getActive();
			}

			@Override
			public TranslatorService getTranslator() {
				return translator;
			}

			@Override
			public int getItemEnumeration(ExplorationItem item) {
				return display.getPosition(item.getValue());
			}

		}), translator, null, mode);
		this.display = display;
        addStyleName(StyleName.WIDGET);
		createComponents();
		refresh();
		hook();
	}

	private void createComponents() {
		this.setWidth("100%");
		this.addClickHandler(new ClickHandler<ExplorationItemDisplay>() {
			@Override
			public void onClick(int index, ExplorationItemDisplay operation) {
				explore(operation);
			}
		});
		this.bind();
	}

	private void explore(final ExplorationItemDisplay explorationItemDisplay) {
		if (timer != null) timer.cancel();
		timer = new Timer() {
			@Override
			public void run() {
				display.explore(explorationItemDisplay);
			}
		};
		timer.schedule(DELAY_TAB_CHANGE);
	}

	private void refresh() {
		this.clear();

		for (Presenter presenter : this.display) {
			ExplorationItemDisplay explorationItemDisplay = (ExplorationItemDisplay)presenter;
			ExplorationItem explorationItem = (ExplorationItem)this.addItem(explorationItemDisplay);

			if (this.display.getActive() == explorationItemDisplay)
				explorationItem.activate();

			if (this.display.isItemInActiveOperation(explorationItemDisplay))
				explorationItem.highlight();
		}
	}

	private void refreshActive(ExplorationItemDisplay explorationItemDisplay) {
		this.deactivateAll();

		ExplorationItem item = this.getItem(explorationItemDisplay);
		if (item != null)
			item.activate();

		highlightRoot(explorationItemDisplay);
	}

	private void highlightRoot(ExplorationItemDisplay explorationItemDisplay) {
		ExplorationItem rootItem = getItem(display.getRoot(explorationItemDisplay));
		if (rootItem != null)
			rootItem.highlight();
	}

	private void refreshLinkOperation(ExplorationItemDisplay explorationItemDisplay) {

		ExplorationItem explorationItem = this.getItem(explorationItemDisplay);
		if (explorationItem == null)
			return;

		explorationItem.link();
	}

	private void deactivateAll() {
		for (Widget child : this.getItems()) {
			if (!(child instanceof ExplorationItem)) continue;
			ExplorationItem childExplorationItem = (ExplorationItem)child;
			childExplorationItem.deactivate();
		}
	}

	@Override
	public ExplorationItem getItem(ExplorationItemDisplay explorationItemDisplay) {
		for (Widget child : this.getItems()) {
			if (!(child instanceof ExplorationItem)) continue;
			ExplorationItem result = getItem(explorationItemDisplay, (ExplorationItem) child);
			if (result != null)
				return result;
		}
		return null;
	}

	private ExplorationItem getItem(ExplorationItemDisplay explorationItemDisplay, ExplorationItem item) {

		if (item.isFor(explorationItemDisplay))
			return item;

		for (Widget child : item.getItems()) {
			if (!(child instanceof ExplorationItem)) continue;
			ExplorationItem result = getItem(explorationItemDisplay, (ExplorationItem) child);
			if (result != null)
				return result;
		}

		return null;
	}

	private void hook() {
		this.display.addHook(new ExplorationDisplay.Hook() {
			@Override
			public void update() {
				refresh();
			}

			@Override
			public void activate(ExplorationItemDisplay item) {
				refreshActive(item);
			}

			@Override
			public void link(ExplorationItemDisplay item) {
				refreshLinkOperation(item);
			}

			@Override
			public void unLink(ExplorationItemDisplay item) {
				refresh();
			}
		});
	}

	private void bind() {
		this.onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	public static class EnumerateExplorationItem extends ExplorationItem {
		public EnumerateExplorationItem(ExplorationItemDisplay value, ExplorationItem owner, ExplorationWidgetControl control) {
			super(value, owner, control);
		}

		@Override
		protected Widget createComponent() {
			HorizontalPanel component = new HorizontalPanel();
			component.add(createEnumeration());
			component.add(super.createComponent());
			return component;
		}

		private Widget createEnumeration() {
			Label label = new Label(String.valueOf(control.getItemEnumeration(this) + 1));
			label.addStyleName(StyleName.NUMBER);
			label.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					notifyClick();
					event.stopPropagation();
				}
			});
			return label;
		}
	}

	public static class ExplorationItem extends ListItem<ExplorationItemDisplay> {
		private final ExplorationItem owner;
		protected final ExplorationWidgetControl control;
		private HTMLListWidget<ExplorationItemDisplay> children;

		public ExplorationItem(ExplorationItemDisplay value, ExplorationItem owner, ExplorationWidgetControl control) {
			super(value);
			this.owner = owner;
			this.control = control;
			init();
		}

		@Override
		protected void init() {
			super.init();
			if (value.isSpaced())
				addStyleName(StyleName.SPACE);
		}

		public void activate() {
			addStyleName(StyleName.ACTIVE);
		}

		public void highlight() {
			addStyleName(StyleName.HIGHLIGHT);
		}

		public boolean isLinked() {
			return this.value.isLinked(false);
		}

		private void refreshLinkState() {
			boolean linked = isLinked();
			removeStyleName(StyleName.LINKED);
			if (linked)
				addStyleName(StyleName.LINKED);
		}

		public void deactivate() {
			this.removeStyleName(StyleName.ACTIVE);
			this.removeStyleName(StyleName.HIGHLIGHT);

			for (Widget child : this.children.getItems()) {
				if (!(child instanceof ExplorationItem)) continue;
				((ExplorationItem)child).deactivate();
			}
		}

		public boolean isFor(ExplorationItemDisplay operation) {
			return this.value == operation;
		}

		@Override
		protected Widget[] createControlOperations() {
			return new Widget[0];
		}

		@Override
		protected Widget createComponent() {
			HTMLPanel component = new HTMLPanel("");
			component.add(createLabelBox());
			component.add(createChildren());
			return component;
		}

		private Widget createLabelBox() {
			HTMLPanel labelBox = new HTMLPanel("");
			labelBox.getElement().getStyle().setPosition(Position.RELATIVE);
			labelBox.add(createLabel());
			labelBox.add(createLink());
			return labelBox;
		}

		private Anchor createLabel() {
			Anchor label = new Anchor(getLabel());
			label.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					notifyClick();
					event.stopPropagation();
				}
			});
			return label;
		}

		private Anchor createLink() {
			Anchor link = new Anchor();
			link.addStyleName(StyleName.LINK);
			link.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {

					if (isLinked())
						value.unLink();
					else
						value.link();

					event.stopPropagation();
				}
			});

			refreshLinkState();

			return link;
		}

		private Widget createChildren() {
			TranslatorService translator = control.getTranslator();
			children = new HTMLListWidget<>(new Builder(this, control), translator);

			children.addStyleName(StyleName.CHILDREN);
			children.addClickHandler(new HTMLListWidget.ClickHandler<ExplorationItemDisplay>() {
				@Override
				public void onClick(int index, ExplorationItemDisplay value) {
					notifyChildClick((ExplorationItem)children.getItems().get(index));
				}
			});

			for (Presenter child : value) {
				ExplorationItemDisplay explorationItemDisplay = (ExplorationItemDisplay) child;
				ExplorationItem explorationItem = (ExplorationItem)children.addItem(explorationItemDisplay);

				if (explorationItemDisplay == control.getActive())
					explorationItem.activate();
			}

			return children;
		}

		private void notifyChildClick(ExplorationItem item) {
			ExplorationItem owner = this.getOwner();
			if (owner != null) {
				owner.notifyChildClick(item);
				return;
			}
			notifyClick(item);
		}

		@Override
		protected Widget[] createOperations() {
			return new Widget[0];
		}

		public ExplorationItem getOwner() {
			return owner;
		}

		public WidgetCollection getItems() {
			return children.getItems();
		}

		public void link() {
			refreshLinkState();
		}

		public static class Builder extends ListItem.Builder<ExplorationItemDisplay> {
			private final ExplorationItem owner;
			private final ExplorationWidgetControl control;

			public Builder(ExplorationItem owner, ExplorationWidgetControl control) {
				super();
				this.owner = owner;
				this.control = control;
			}

			public Builder(ExplorationWidgetControl control) {
				this(null, control);
			}

			@Override
			public ListItem<ExplorationItemDisplay> build(ExplorationItemDisplay value, Mode mode) {
				if (mode == Mode.LIST_ENUMERATE)
					return new EnumerateExplorationItem(value, owner, control);

				return new ExplorationItem(value, owner, control);
			}
		}

		protected String getLabel() {
			return value.getLabel();
		}

		protected interface StyleName extends ListItem.StyleName {
			String ACTIVE = "active";
			String HIGHLIGHT = "highlight";
			String LINKED = "linked";
			String LINK = "link";
			String CHILDREN = "children";
			String NUMBER = "number";
		}

	}

	public static class Builder extends cosmos.gwt.presenters.Presenter.Builder<TranslatorService> {

		@Override
		public boolean canBuild(Presenter presenter, String design) {
			return presenter.is(ExplorationDisplay.TYPE) && design.equals("tab");
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			Mode mode = layout.equals("enumeration")?Mode.LIST_ENUMERATE:Mode.LIST;
			ExplorationWidget widget = new ExplorationWidget((ExplorationDisplay) presenter, translator, mode);

			if (!layout.isEmpty())
				widget.getElement().addClassName("tab");

			return widget;
		}
    }

	public interface ExplorationWidgetControl {
		ExplorationItemDisplay getActive();
		TranslatorService getTranslator();
		int getItemEnumeration(ExplorationItem item);
	}

	public interface StyleName extends HTMLListWidget.StyleName {
		String WIDGET = "exploration-widget";
	}
}
