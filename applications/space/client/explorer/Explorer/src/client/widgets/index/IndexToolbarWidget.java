package client.widgets.index;

import client.core.model.Filter;
import client.core.model.IndexEntry;
import client.core.model.List;
import client.presenters.displays.IndexDisplay;
import client.presenters.displays.view.ViewDisplay;
import client.services.TranslatorService;
import client.widgets.commands.CommandListWidget;
import client.widgets.commands.CommandWidget;
import client.widgets.commands.CompositeCommandWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import cosmos.gwt.widgets.CosmosHtmlPanel;
import cosmos.presenters.Operation;
import cosmos.presenters.Presenter;

import static client.widgets.commands.CompositeCommandWidget.Configuration;

public class IndexToolbarWidget<D extends IndexDisplay, E extends IndexEntry> extends CosmosHtmlPanel {
	protected final D display;
	protected final TranslatorService translator;
	protected CommandListWidget toolbar;
	private Label selectionMessage;
    private Label numberOfItemsMessage;

    public IndexToolbarWidget(D display, TranslatorService translator) {
		addStyleName(Style.WIDGET);
		this.display = display;
		this.translator = translator;
		createComponents();
		refresh();
		hook();
	}

	protected void createComponents() {
		add(createToolbar());
        add(createNumberOfItemsMessage());
		add(createSelectionMessage());

		bind();
	}

    protected Widget createToolbar() {
		toolbar = new CommandListWidget(translator, CommandListWidget.Design.TOOLBAR_BOTTOM);
		createToolbarItems();
		return toolbar;
	}

	protected void createToolbarItems() {
		Configuration configuration = createDialogPositionCalculator();
        CommandWidget.Builder builder = Builder.getCommandBuilder();
		List<Operation> operations = display.getOperations();

        for (Operation operation : operations) {
            CommandWidget commandWidget = (CommandWidget)builder.build(operation, "link", "link");

	        if (commandWidget instanceof CompositeCommandWidget)
                ((CompositeCommandWidget) commandWidget).setConfiguration(configuration);

            toolbar.addItem(commandWidget);
        }
	}

    private Widget createNumberOfItemsMessage() {
        numberOfItemsMessage = new Label();
        numberOfItemsMessage.addStyleName(Style.SELECTION_MESSAGE);
        numberOfItemsMessage.setVisible(false);
        return numberOfItemsMessage;
    }

	protected Widget createSelectionMessage() {
		selectionMessage = new Label();
		selectionMessage.addStyleName(Style.SELECTION_MESSAGE);
		selectionMessage.setVisible(false);
		return selectionMessage;
	}

	private static Configuration createDialogPositionCalculator() {
		return new Configuration() {
			private static final int BORDER_SIZE = 1;

			@Override
			public int getDialogTop(CompositeCommandWidget command, CompositeCommandWidget.Dialog dialog) {
				return -1;
			}

			@Override
			public int getDialogMarginTop(CompositeCommandWidget command, CompositeCommandWidget.Dialog dialog) {
				int offset = (command.getParentCommand() instanceof CompositeCommandWidget) ? BORDER_SIZE * 4 * 2 : BORDER_SIZE * 4;
				return -(dialog.getOffsetHeight()+command.getOffsetHeight()+offset);
			}

			@Override
			public int getDialogLeft(CompositeCommandWidget command, CompositeCommandWidget.Dialog dialog) {
				return 0;
			}

			@Override
			public int getDialogMarginLeft(CompositeCommandWidget command, CompositeCommandWidget.Dialog dialog) {
				return 0;
			}

			@Override
			public CompositeCommandWidget.Design getDesign() {
				return CompositeCommandWidget.Design.BOTTOM;
			}
		};
	}

	protected void refresh() {
		selectionMessage.setVisible(display.getSelectionCount() > 0);
		selectionMessage.setText(translator.getSelectionCountLabel(display.getSelectionCount()));
	}

    private void refreshNumberOfEntries(int entriesCount) {
        numberOfItemsMessage.setVisible(entriesCount > 0);
        numberOfItemsMessage.setText(translator.getCountLabel(entriesCount));
    }

	protected void hook() {
		display.addHook(new D.Hook<E>() {
			@Override
			public void clear() {
			}

			@Override
			public void loadingPage() {
			}

			@Override
			public void page(D.Page<E> page) {
			}

			@Override
			public void pagesCount(int count) {
                refreshNumberOfEntries(display.getEntriesCount());
			}

			@Override
			public void pageEntryAdded(E entry) {
			}

			@Override
			public void pageEntryDeleted() {
			}

			@Override
			public void pageFailure() {
			}

			@Override
			public void pageEntryUpdated(E entry) {
			}

			@Override
			public void entityView(ViewDisplay entityViewDisplay) {
			}

			@Override
			public void selectEntries(List<E> entries) {
				refresh();
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

    protected void bind() {
		this.onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	public static class Builder extends cosmos.gwt.presenters.Presenter.Builder<TranslatorService> {
        private static CommandWidget.Builder commandBuilder;
		public static final String DESIGN = "toolbar";

		@Override
		public boolean canBuild(Presenter presenter, String design) {
			return presenter.is(IndexDisplay.TYPE) && (design!=null && design.equals(DESIGN));
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			createBuilders();

            Widget widget = buildWidget(presenter, design, layout);
            if (!layout.isEmpty()) widget.addStyleName(layout);
			return widget;
		}

        private Widget buildWidget(Presenter presenter, String design, String layout) {
            Builder builder = getChildBuilder(presenter, design, layout, translator, theme);
            if (builder != null)
                return builder.build(presenter, design, layout);
            return new IndexToolbarWidget<>((IndexDisplay) presenter, translator);
        }

        private void createBuilders() {
			if (commandBuilder != null)
				return;
			commandBuilder = new CommandWidget.Builder();
			commandBuilder.inject(translator);
			commandBuilder.inject(theme);
		}

		protected static CommandWidget.Builder getCommandBuilder() {
			return commandBuilder;
		}

	}

	public interface Style {
		String WIDGET = "index-toolbar-widget";
		String SELECTION_MESSAGE = "selection-message";
	}

}
