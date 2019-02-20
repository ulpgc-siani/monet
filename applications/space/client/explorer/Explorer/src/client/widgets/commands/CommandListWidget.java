package client.widgets.commands;

import client.presenters.displays.OperationListDisplay;
import client.services.TranslatorService;
import client.widgets.toolbox.HTMLListWidget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

public class CommandListWidget extends HTMLListWidget<CommandWidget> {

	public enum Design {
		TOOLBAR_BOTTOM, MENU;

		@Override
		public String toString() {
			return super.toString().replace("_", "-").toLowerCase() + "-design";
		}

	}

	public CommandListWidget(TranslatorService translator) {
		this(translator, null);
	}

	public CommandListWidget(TranslatorService translator, Design design) {
		super(new CommandItem.Builder(translator), translator, "");
		addClasses(design);
	}

	public static abstract class Builder extends cosmos.gwt.presenters.Presenter.Builder<TranslatorService> {

		@Override
		public boolean canBuild(Presenter presenter, String design) {
			return presenter.is(OperationListDisplay.TYPE);
		}
	}

	public static class CommandItem<T extends CommandWidget> extends ListItem<T> {

		protected final TranslatorService translator;

		public CommandItem(T value, TranslatorService translator) {
			super(value);
			this.addStyleName(CommandListWidget.Style.COMMAND);
			this.translator = translator;
			init();
		}

		@Override
		protected Widget[] createControlOperations() {
			return new Widget[0];
		}

		@Override
		protected Widget createComponent() {
            value.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    notifyClick();
                }
            });
			return value;
		}

		@Override
		protected Widget[] createOperations() {
			return new Widget[0];
		}

		public static class Builder extends ListItem.Builder<CommandWidget> {

			private final TranslatorService translator;

            public Builder(TranslatorService translator) {
				super();
				this.translator = translator;
            }

			@Override
			public ListItem<CommandWidget> build(CommandWidget commandWidget, Mode mode) {
				return new CommandItem<>(commandWidget, translator);
			}

		}
		protected String getLabel() {
			return value.getLabel();
		}

	}

	public interface Style {

		String WIDGET_NAME = "command-list";
		String COMMAND = "command";
	}

	private void addClasses(Design design) {
		addStyleName(Style.WIDGET_NAME);
		if (design != null)
            addStyleName(design.toString());
	}

}
