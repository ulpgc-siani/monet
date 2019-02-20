package client.presenters.displays.view;

import client.core.model.*;
import client.core.model.definition.views.FormViewDefinition;
import client.core.system.MonetList;
import client.presenters.displays.entity.FieldDisplay;
import client.services.NotificationService;
import client.services.TranslatorService;
import cosmos.presenters.Presenter;
import cosmos.presenters.RootDisplay;
import cosmos.types.Date;
import cosmos.utils.StringUtils;

import static client.services.TranslatorService.OperationLabel;

public class FormViewDisplay extends NodeViewDisplay<FormView> implements NotificationService.MessageSource {

	public static final Type TYPE = new Type("FormViewDisplay", NodeViewDisplay.TYPE);
	private boolean showMore;
	private Date lastSave = null;
    private NotificationService notificationService;

    public FormViewDisplay(Node node, FormView nodeView) {
        super(node, nodeView);
	}

	@Override
	protected void onInjectServices() {
		if (getView() instanceof ProxyView) return;

		addFields();
        services.getNodeService().focusNodeView(getEntity());
        services.getNotificationService().registerSource(this);
		showMore = false;
	}

    public String getId() {
        return getEntity().getDefinition().getCode();
    }

	public List<FieldDisplay> getNonExtendedFields() {
		return getFields(new Condition() {
			@Override
			public boolean evaluate(FieldDisplay display) {
				return !display.isExtended();
			}
		});
	}

	public List<FieldDisplay> getExtendedFields() {
		return getFields(new Condition() {
			@Override
			public boolean evaluate(FieldDisplay display) {
				return display.isExtended();
			}
		});
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	public String getLayout() {
		if (getViewDefinition() == null || getViewDefinition().getShow() == null)
			return null;
		return getViewDefinition().getShow().getLayout();
	}

	public String getLayoutExtended() {
		if (getViewDefinition() == null || getViewDefinition().getShow() == null)
			return null;
		return getViewDefinition().getShow().getLayoutExtended();
	}

	@Override
	public void setRootDisplay(RootDisplay rootDisplay) {
		super.setRootDisplay(rootDisplay);
	}

    @Override
    public void register(NotificationService service) {
        notificationService = service;
    }

	private FormViewDefinition getViewDefinition() {
		return getView().getDefinition();
	}

	private void addFields() {
		FormView formView = getView();
		Form form = formView.getScope();

		for (Field field : formView.getShows())
			addChild(createDisplay(form, field));
	}

	private FieldDisplay createDisplay(final Form form, final Field field) {
		final FieldDisplay display = new FieldDisplay.Builder().build(form, field);
		display.addValueChangeHandler(new FieldDisplay.ValueChangeHandler() {
            @Override
            public void onChange() {
                notifyChange();
                lastSave = new Date();
                refreshMessageWhenSave();
            }
        });
		return display;
	}

    private void notifyChange() {
        notificationService.notify(NotificationService.MessageType.UPDATE_NODE, new NotificationService.Message() {
            @Override
            public String getString(String name) {
                return null;
            }

            @Override
            public int getInteger(String name) {
                return 0;
            }

            @Override
            public Node getNode() {
                return getRoot();
            }
        });
	}

	public boolean hasExtendedFields() {
		for (Presenter child : this) {
			if (child instanceof FieldDisplay && ((FieldDisplay)child).isExtended())
				return true;
		}
		return false;
	}

	public void toggleShowMore() {
		showMore = !showMore;
		notifyShow();
		notifyScroll();
	}

    public void scroll(int position) {
		getLayoutDisplay().scroll(position);
	}

	private List<FieldDisplay> getFields(Condition condition) {
		List<FieldDisplay> result = new MonetList<>();

		for (Object child : this) {
			if (child instanceof FieldDisplay && condition.evaluate((FieldDisplay) child))
				result.add((FieldDisplay)child);
		}

		return result;
	}

	public String getShowAllLabel() {
		if (showMore)
			return services.getTranslatorService().translate(OperationLabel.SHOW_LESS_FIELDS);
		return services.getTranslatorService().translate(OperationLabel.SHOW_MORE_FIELDS);
	}

	public boolean hasLayout() {
		return getLayout() != null && !getLayout().isEmpty();
	}

	public boolean hasLayoutExtended() {
		return getLayoutExtended() != null && !getLayoutExtended().isEmpty();
	}

	public boolean getShowAllValue() {
		return showMore;
	}

    private Node getRoot() {
        Node entity = getEntity();
        while (entity.isComponent())
            entity = (Node) entity.getOwner();
        return entity;
    }

    public static class Builder extends ViewDisplay.Builder<Node, FormView> {

		protected static void register() {
			registerBuilder(Form.CLASS_NAME.toString() + FormView.CLASS_NAME.toString(), new Builder());
		}

		@Override
		public ViewDisplay build(Node entity, FormView view) {
			return new FormViewDisplay(entity instanceof Container ? view.getScope() : entity, view);
		}
	}

	public interface Hook extends NodeViewDisplay.Hook {
		void show();
		void failure(String error);
		void scroll();
		void timer(int delay);
		void cancelTimer();
	}

	@Override
	public void presenterRemoved(Presenter presenter) {
		super.presenterRemoved(presenter);
		notifyCancelTimer();
	}

	private interface Condition {
		boolean evaluate(FieldDisplay display);
	}

	private void notifyShow() {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.show();
			}
		});
	}

	private void notifyScroll() {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.scroll();
			}
		});
	}

	private void notifyTimer(final int delay) {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.timer(delay);
			}
		});
	}

	private void notifyCancelTimer() {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.cancelTimer();
			}
		});
	}

	public void refreshMessageWhenSave() {
		String label = getEntity().getLabel();
		TranslatorService translator = services.getTranslatorService();
		String message = translator.translateSavedPeriodAgo(label, lastSave);
		String shortMessage = translator.translateSavedPeriodAgo(StringUtils.shortContent(label, 20), lastSave);

		showMessage(message, shortMessage);

		notifyTimer(6000);
	}
}
