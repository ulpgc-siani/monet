package client.widgets.commands;

import client.presenters.displays.OperationListDisplay;
import client.presenters.operations.Operation;
import client.services.TranslatorService;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

import static client.widgets.commands.CompositeCommandWidget.Configuration;

public class ToolbarWidget extends CommandListWidget {
	private final OperationListDisplay display;

    public ToolbarWidget(OperationListDisplay display, TranslatorService translator) {
		super(translator, CommandListWidget.Design.TOOLBAR_BOTTOM);
		this.display = display;
		createComponents();
		refresh();
		hook();
	}

	private void createComponents() {
		setWidth("100%");
		bind();
	}

	private void refresh() {
		Configuration configuration = createDialogPositionCalculator();

		clear();
        CommandWidget.Builder builder = Builder.getCommandBuilder();
		for (Operation operation : display.getOperations()) {
			CommandWidget commandWidget = (CommandWidget)builder.build(operation, "link", "link");

			if (commandWidget instanceof CompositeCommandWidget)
                ((CompositeCommandWidget) commandWidget).setConfiguration(configuration);

			addItem(commandWidget);
		}
	}

	private void hook() {
		display.addHook(new OperationListDisplay.Hook() {
		});
	}

	private void bind() {
		this.onAttach();
		RootPanel.detachOnWindowClose(this);
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
				int offset = (command.getParentCommand() instanceof CompositeCommandWidget)?BORDER_SIZE*4*2:BORDER_SIZE*4;
				return -(dialog.getOffsetHeight()+command.getOffsetHeight()+offset);
			}

			@Override
			public int getDialogLeft(CompositeCommandWidget command, CompositeCommandWidget.Dialog dialog) {

				if (command.getParentCommand() instanceof CompositeCommandWidget)
					return -BORDER_SIZE * 3;

				return command.getAbsoluteLeft();
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

	public static class Builder extends CommandListWidget.Builder {
		private static CommandWidget.Builder commandBuilder;

		@Override
		public boolean canBuild(Presenter presenter, String design) {
            return super.canBuild(presenter, design) && design.equals("toolbar");
        }

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
            createBuilders();

			Widget toolbarWidget = new ToolbarWidget((OperationListDisplay) presenter, translator);

			if (!layout.isEmpty())
				toolbarWidget.addStyleName(layout);

			return toolbarWidget;
		}

		public static CommandWidget.Builder getCommandBuilder() {
			return commandBuilder;
		}

		private void createBuilders() {
			commandBuilder = new CommandWidget.Builder();
			commandBuilder.inject(theme);
			commandBuilder.inject(translator);
		}
	}
}
