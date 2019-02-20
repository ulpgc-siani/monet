package client.widgets.commands;

import client.presenters.displays.OperationListDisplay;
import client.presenters.operations.Operation;
import client.services.TranslatorService;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

import static client.widgets.commands.CompositeCommandWidget.Configuration;

public class MenuWidget extends CommandListWidget {
	private final OperationListDisplay display;

	public MenuWidget(OperationListDisplay display, TranslatorService translator) {
		super(translator, CommandListWidget.Design.MENU);
		this.display = display;
		this.createComponents();
		this.refresh();
		this.hook();
	}

	private void createComponents() {
		this.setWidth("100%");
		this.bind();
	}

	private void refresh() {
		Configuration configuration = createDialogPositionCalculator();

		this.clear();
		for (Operation operation : this.display.getOperations()) {
			CommandWidget.Builder builder = Builder.getCommandBuilder();
			CommandWidget commandWidget = (CommandWidget)builder.build(operation, "link", "link");

			if (commandWidget instanceof CompositeCommandWidget) {
				CompositeCommandWidget widget = (CompositeCommandWidget) commandWidget;
				widget.setConfiguration(configuration);
			}

			this.addItem(commandWidget);
		}
	}

	private void hook() {
		this.display.addHook(new OperationListDisplay.Hook() {
		});
	}

	private void bind() {
		this.onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	private Configuration createDialogPositionCalculator() {
		return new Configuration() {
			@Override
			public int getDialogTop(CompositeCommandWidget command, CompositeCommandWidget.Dialog dialog) {
				return -1;
			}

			@Override
			public int getDialogMarginTop(CompositeCommandWidget command, CompositeCommandWidget.Dialog dialog) {
				return -command.getOffsetHeight();
			}

			@Override
			public int getDialogLeft(CompositeCommandWidget command, CompositeCommandWidget.Dialog dialog) {
				return -1;
			}

			@Override
			public int getDialogMarginLeft(CompositeCommandWidget command, CompositeCommandWidget.Dialog dialog) {
				return command.getOffsetWidth();
			}

			@Override
			public CompositeCommandWidget.Design getDesign() {
				return CompositeCommandWidget.Design.RIGHT;
			}
		};
	}

	public static class Builder extends CommandListWidget.Builder {
		private static CommandWidget.Builder commandBuilder;

		@Override
		public boolean canBuild(Presenter presenter, String design) {
            return super.canBuild(presenter, design) && design.equals("menu");
        }

		@Override
		public Widget build(Presenter presenter, String design, String layout) {

			createBuilders();

			Widget menuWidget = new MenuWidget((OperationListDisplay) presenter, translator);

			if (!layout.isEmpty())
				menuWidget.getElement().addClassName(layout);

			return menuWidget;
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

	public interface Style extends CommandListWidget.Style {
	}

}
