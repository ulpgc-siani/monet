package client.widgets.commands;

import client.presenters.operations.CompositeOperation;
import client.presenters.operations.Operation;
import client.services.TranslatorService;
import client.widgets.popups.PopUpWidget;
import client.widgets.toolbox.HTMLListWidget;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Widget;

import static cosmos.gwt.utils.WidgetUtils.bindWidgetToElement;

public abstract class CompositeCommandWidget<W extends FocusWidget> extends CommandWidget<CompositeOperation, W> {
	private Dialog dialog;
	private Timer timer;
	private Configuration configuration;

	public enum Design {
		BOTTOM, RIGHT;

		@Override
		public String toString() {
			return super.toString().toLowerCase() + "-design";
		}
	}

	public CompositeCommandWidget(CompositeOperation operation, TranslatorService translator, W component, String layout) {
		super(operation, translator, component, layout);
        init();
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
		refreshDesign();
	}

	@Override
    protected void addClasses() {
        super.addClasses();
        addStyleName(StyleName.COMPOSITE);
    }

    @Override
    protected void init() {
        super.init();
        createTimer();
    }

	@Override
	protected void createComponents() {
		super.createComponents();

		addComponentBehaviours();
		add(createDialog());
	}

	protected void addComponentBehaviours() {
		component.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				if (!dialog.isVisible())
					showDialog();

				event.stopPropagation();
			}
		});

		component.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				showDialog();
			}
		});

		component.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				hideDialogDelayed();
			}
		});

	}

	public interface Configuration {

		int getDialogTop(CompositeCommandWidget command, CompositeCommandWidget.Dialog dialog);
		int getDialogMarginTop(CompositeCommandWidget command, CompositeCommandWidget.Dialog dialog);
		int getDialogLeft(CompositeCommandWidget command, CompositeCommandWidget.Dialog dialog);
		int getDialogMarginLeft(CompositeCommandWidget command, CompositeCommandWidget.Dialog dialog);
		Design getDesign();
	}

	private void createTimer() {
		timer = new Timer() {
			@Override
			public void run() {
				hideDialog();
			}
		};
	}

	private Widget createDialog() {
		dialog = new Dialog();
		return dialog;
	}

	protected void refreshDesign() {
		Design design = this.configuration.getDesign();

		if (design == null)
			return;

		this.removeStyleName(Design.BOTTOM.toString());
		this.removeStyleName(Design.RIGHT.toString());

		this.addStyleName(design.toString());
	}

	private void showDialog() {

		if (!component.isEnabled())
			return;

		dialog.show();
		dialog.setTop(configuration.getDialogTop(this, dialog));
		dialog.setMarginTop(configuration.getDialogMarginTop(this, dialog));
		dialog.setLeft(configuration.getDialogLeft(this, dialog));
		dialog.setMarginLeft(configuration.getDialogMarginLeft(this, dialog));
	}

	private void hideDialog() {
		dialog.hide();
	}

	private void hideDialogDelayed() {
		timer.schedule(200);
	}

	private void clearTimer() {
		timer.cancel();
	}

	public class Dialog extends PopUpWidget<FocusPanel> {

		public Dialog() {
			super();
			addStyleName(CompositeCommandWidget.StyleName.DIALOG);
			createSizeCalculator();
			init();
		}

		@Override
		protected void init() {
			super.init();
			createContent();
		}

		@Override
		protected FocusPanel createContent(Element container) {
			FocusPanel content = new FocusPanel();
			bindWidgetToElement(this, content, container);
			return content;
		}

		private void createSizeCalculator() {
			setSizeCalculator(new PopUpWidget.SizeCalculator() {
				@Override
				public int getWidth() {
					return -1;
				}

				@Override
				public int getHeight() {
					return -1;
				}
			});
		}

		private void createContent() {
			final CommandListWidget commandListWidget = new CommandListWidget(translator);

			commandListWidget.addClickHandler(new HTMLListWidget.ClickHandler<CommandWidget>() {
				@Override
				public void onClick(int index, CommandWidget value) {
					if (!(value instanceof CompositeCommandWidget))
						hideDialog();
				}
			});

			for (Object childOperation : operation.getOperations()) {
				CommandWidget.Builder builder = CommandWidget.Builder.get();
				CommandWidget commandWidget = (CommandWidget)builder.build((Operation)childOperation, layout, layout);

				if (commandWidget instanceof CompositeCommandWidget) {
					CompositeCommandWidget compositeCommandWidget = (CompositeCommandWidget) commandWidget;
					compositeCommandWidget.setConfiguration(createDialogConfiguratorDelegate());
				}

				commandListWidget.addItem(commandWidget);
			}

			getContent().add(commandListWidget);

			addContentEvents();
		}

		private void addContentEvents() {
			getContent().addMouseOverHandler(new MouseOverHandler() {
				@Override
				public void onMouseOver(MouseOverEvent event) {
					clearTimer();
				}
			});
			getContent().addMouseOutHandler(new MouseOutHandler() {
				@Override
				public void onMouseOut(MouseOutEvent event) {
					hideDialogDelayed();
				}
			});
		}

		public void setTop(int top) {
			if (top != -1)
				this.getStyleElement().getStyle().setTop(top, com.google.gwt.dom.client.Style.Unit.PX);
		}

		public void setMarginTop(int marginTop) {
			if (marginTop != -1)
				this.getStyleElement().getStyle().setMarginTop(marginTop, com.google.gwt.dom.client.Style.Unit.PX);
		}

		public void setLeft(int left) {
			if (left != -1)
				this.getStyleElement().getStyle().setLeft(left, com.google.gwt.dom.client.Style.Unit.PX);
		}

		public void setMarginLeft(int marginLeft) {
			if (marginLeft != -1)
				this.getStyleElement().getStyle().setMarginLeft(marginLeft, com.google.gwt.dom.client.Style.Unit.PX);
		}
	}

    private Configuration createDialogConfiguratorDelegate() {
        return new Configuration() {
            @Override
            public int getDialogTop(CompositeCommandWidget command, CompositeCommandWidget.Dialog dialog) {
                return configuration.getDialogTop(command, dialog);
            }

            @Override
            public int getDialogMarginTop(CompositeCommandWidget command, CompositeCommandWidget.Dialog dialog) {
                return configuration.getDialogMarginTop(command, dialog);
            }

            @Override
            public int getDialogLeft(CompositeCommandWidget command, CompositeCommandWidget.Dialog dialog) {
                return configuration.getDialogLeft(command, dialog);
            }

            @Override
            public int getDialogMarginLeft(CompositeCommandWidget command, CompositeCommandWidget.Dialog dialog) {
                return configuration.getDialogMarginLeft(command, dialog);
            }

	        @Override
	        public Design getDesign() {
		        if (configuration != null)
		            return configuration.getDesign();
		        return null;
	        }
        };
    }

    static class StyleName {
		public static final String DIALOG = "dialog";
		public static final String COMPOSITE = "composite";
	}
}
