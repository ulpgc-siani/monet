package client.presenters.displays;

import client.core.model.Entity;
import client.core.model.Node;
import client.core.model.NodeView;
import client.presenters.operations.Operation;
import client.presenters.operations.ShowNodeOperation;
import client.services.NotificationService;
import client.services.callback.Callback;
import cosmos.presenters.Presenter;

import static cosmos.presenters.Operation.Context;

public class EntityVisitingDisplay extends VisitingDisplay<EntityDisplay> {

	public static final Type TYPE = new Type("EntityVisitingDisplay", VisitingDisplay.TYPE);

	public EntityVisitingDisplay(EntityDisplay display, cosmos.presenters.Operation from) {
		super(display, from);
	}

	@Override
	protected void onInjectServices() {
        services.getNotificationService().registerListener(new NotificationService.UpdateNodeListener() {
            @Override
            public void notify(Node node) {
                if (getDisplay() != null && node.equals(getDisplay().getEntity()))
                    loadNodeLabel();
            }
        });
	}

	@Override
	public String getLabel() {
		EntityDisplay display = getDisplay();

		if (display == null)
			return "";

		return display.getLabel();
	}

    public void up() {
		EntityDisplay display = getDisplay();
		if (display == null)
			return;

        Entity owner = display.getEntity().getOwner();
		if (owner == null)
			return;

		if (owner instanceof Node)
           executeShowNodeOperation((Node) owner);
	}

    private void executeShowNodeOperation(Node owner) {
        Operation operation = new ShowNodeOperation(new Context() {
            @Override
            public Presenter getCanvas() {
                return getRootDisplay();
            }

            @Override
            public cosmos.presenters.Operation getReferral() {
                return toOperation();
            }
        }, owner, null);

        operation.inject(services);
        operation.execute();
    }

    public boolean isUpEnabled() {
		EntityDisplay display = getDisplay();

		if (display == null)
			return false;

		if (services.getAccountService().load().getRootNode().equals(display.getEntity()))
			return false;

		return display.getEntity().getOwner() != null;
	}

    @Override
	public void back() {
		from.execute();
	}

	public boolean isBackEnabled() {
		return from != null;
	}

	@Override
	public Operation toOperation() {
		Operation operation = null;
		EntityDisplay display = getDisplay();

		if (display == null)
			return null;

		Entity entity = display.getEntity();
		if (entity instanceof Node)
			operation = createNodeOperation((Node) entity);

		if (operation != null)
			operation.inject(services);

		return operation;
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	public boolean isEntityComponent() {

		if (getDisplay() == null)
			return false;

		Entity entity = getDisplay().getEntity();
		if (entity == null)
			return false;

		if (! (entity instanceof client.core.model.Node))
			return false;

		return ((client.core.model.Node) entity).isComponent();
	}

	private void loadNodeLabel() {
		final Node entity = (Node) getDisplay().getEntity();

		services.getNodeService().getLabel(entity, new Callback<String>() {
			@Override
			public void success(String object) {
				entity.setLabel(object);
				notifyLabel();
			}

			@Override
			public void failure(String error) {
				notifyLabelFailure(error);
			}
		});
	}

    private Operation createNodeOperation(Node entity) {
		return new ShowNodeOperation(new Context() {
			@Override
			public Presenter getCanvas() {
				return EntityVisitingDisplay.this.getRootDisplay();
			}

			@Override
			public cosmos.presenters.Operation getReferral() {
				return from;
			}
		}, entity, (NodeView)getDisplay().getView());
	}

	private void notifyLabel() {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.label();
			}
		});
	}

	private void notifyLabelFailure(final String error) {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.labelFailure(error);
			}
		});
	}

    public interface Hook extends VisitingDisplay.Hook {
	}
}
