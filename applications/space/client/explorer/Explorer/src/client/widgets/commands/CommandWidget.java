package client.widgets.commands;

import client.presenters.operations.CompositeOperation;
import client.presenters.operations.Operation;
import client.services.TranslatorService;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import cosmos.gwt.widgets.CosmosHtmlPanel;
import cosmos.model.ServerError;
import cosmos.presenters.Presenter;

public abstract class CommandWidget<O extends Operation, W extends FocusWidget> extends CosmosHtmlPanel {
	protected final O operation;
	protected final W component;
	protected final TranslatorService translator;
	protected final String layout;

	public CommandWidget(O operation, TranslatorService translator, W component, String layout) {
		this.operation = operation;
		this.translator = translator;
		this.component = component;
		this.layout = layout;
        addClasses();
	}

    public String getLabel() {
        return operation.getLabel();
    }

    public void execute() {
        if (!component.isEnabled())
            return;

        operation.execute();
    }

    public CommandWidget getParentCommand() {
		Widget parent = this.getParent();

		while (parent != null && !(parent instanceof CommandWidget))
			parent = parent.getParent();

		return (CommandWidget)parent;
	}

    public void addClickHandler(ClickHandler handler) {
        component.addClickHandler(handler);
    }

    protected void addClasses() {
        addStyleName(Style.COMMAND, layout, operation.getName());
    }

    protected void init() {
        createComponents();
        refresh();
        hook();
    }

    protected void createComponents() {
        fillText();
        component.setTitle(operation.getDefaultLabel());
        component.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                execute();
            }
        });
        add(component);

	    bind();
    }

    protected abstract void fillText();

	private void bind() {
		onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	private void refresh() {
		component.setEnabled(operation.enabled());
		component.setVisible(operation.visible());
		refreshStyle();
	}

	private void refreshStyle() {
		if (component.isEnabled())
			component.removeStyleName(Style.DISABLED);
		else
			component.addStyleName(Style.DISABLED);
	}

	private void hook() {
		operation.addHook(new Operation.Hook() {
			@Override
			public void show() {
				refresh();
			}

			@Override
			public void hide() {
				refresh();
			}

			@Override
			public void enable() {
				refresh();
			}

			@Override
			public void disable() {
				refresh();
			}

			@Override
			public void executePerformed() {
			}

			@Override
			public void executeFailed(ServerError details) {
			}
		});
	}

	public static class Builder extends cosmos.gwt.presenters.Presenter.Builder<TranslatorService> {
		private static Builder instance;

		@Override
		public boolean canBuild(Presenter presenter, String design) {
			return presenter.is(Operation.TYPE);
		}

		@Override
		public com.google.gwt.user.client.ui.Widget build(Presenter presenter, String design, String layout) {
			createInstance();

			Widget widget = buildCompositeCommand(presenter, layout);
			if (widget != null)
				return widget;

			if (layout.equals("link"))
				return new LinkCommandWidget((Operation) presenter, translator, layout);

			if (layout.equals("icon"))
				return new IconCommandWidget((Operation) presenter, translator, layout);

			if (layout.equals("button"))
				return new ButtonCommandWidget((Operation) presenter, translator, layout);

			return null;
		}

		private Widget buildCompositeCommand(Presenter presenter, String layout) {

			if (!(presenter instanceof CompositeOperation))
				return null;

			if (layout.equals("link"))
				return new CompositeLinkCommandWidget((CompositeOperation) presenter, translator, layout);

			if (layout.equals("icon"))
				return new CompositeIconCommandWidget((CompositeOperation) presenter, translator, layout);

			if (layout.equals("button"))
				return new CompositeButtonCommandWidget((CompositeOperation) presenter, translator, layout);

			return null;
		}

		public static Builder get() {
			return instance;
		}

		private void createInstance() {
			instance = new Builder();
			instance.inject(theme);
			instance.inject(translator);
		}
	}

	public interface Style {
		String COMMAND = "command";
		String DISABLED = "disabled";
	}
}
