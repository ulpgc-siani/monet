package client.presenters.operations;

import client.core.messages.LoadingNodeError;
import client.core.model.*;
import client.presenters.displays.*;
import client.presenters.displays.entity.NodeDisplay;
import client.services.callback.NodeCallback;
import cosmos.presenters.Presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShowNodeOperation extends ShowOperation<Node, NodeView> {

	public static final Type TYPE = new Type("ShowNodeOperation", Operation.TYPE);

	public ShowNodeOperation(Context context, Node node, NodeView view) {
		super(context, node, view);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void doExecute() {
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, this.getType().toString() + " called. Node: " + entity.getLabel());
		getMessageDisplay().showLoading(services.getTranslatorService().getLoadingLabel());

		services.getNodeService().open(entity.getId(), new NodeCallback() {
			@Override
			public void success(Node object) {
				NodeView view = getNodeView(object);

				object.getViews().update(view);
				setView(view);

				if (object instanceof Container) {
					loadContainerChild((Container) object);
					return;
				}

				getMessageDisplay().hideLoading();
				refresh(object);
			}

			@Override
			public void failure(String error) {
				getMessageDisplay().hideLoading();
				executeFailed(new LoadingNodeError(error));
			}
		});
	}

	protected NodeView getNodeView(Node object) {
		Key viewKey = getView() != null ? getView().getKey() : null;
		if (viewKey == null)
		    return (NodeView) object.getViews().getDefaultView();
	    return (NodeView) object.getViews().get(viewKey);
	}

	protected void loadContainerChild(final Container container) {
		final ContainerView containerView = (ContainerView) getView();
		final NodeView hostView = containerView.getHostView();

		services.getNodeService().open(hostView.getScope().getId(), new NodeCallback() {
			@Override
			public void success(Node object) {
				containerView.setHostView((NodeView) object.getViews().get(hostView.getKey()));

				getMessageDisplay().hideLoading();
				refresh(container);
			}

			@Override
			public void failure(String error) {
				getMessageDisplay().hideLoading();
				executeFailed(new LoadingNodeError(error));
			}
		});
	}

	protected void refresh(Node node) {
		refreshEntity(node);
		refreshExplorationDisplay();
		refreshCanvas(node);
	}

	protected void refreshEntity(Node node) {
		if (!entity.isContainer())
			return;

		if (node.getId().equals(entity.getId()))
			return;

		((Container)entity).addChild(node);
	}

	protected void refreshExplorationDisplay() {
		Presenter display = this.context.getCanvas();

		if (!(display instanceof ApplicationDisplay))
			return;

		List<Operation> operations = createAncestorsOperations();
		operations.add(this);

		ExplorationDisplay explorationDisplay = ((ApplicationDisplay) display).getExplorationDisplay();

		explorationDisplay.clearChildren();
		explorationDisplay.addDeeply(operations.toArray(new Operation[0]), explorationDisplay.getRoot());
		explorationDisplay.activate(this);
	}

	protected void refreshCanvas(Node node) {
		EntityDisplay entityDisplay = new NodeDisplay.Builder().build(node, view);
		EntityVisitingDisplay visitingDisplay = new EntityVisitingDisplay(entityDisplay, context.getReferral());
		Type type = VisitingDisplay.TYPE;
		Presenter display = context.getCanvas();

		if (display.existsChild(type))
			display.removeChild(type);

		display.addChild(visitingDisplay);

		this.executePerformed();
	}

	private List<Operation> createAncestorsOperations() {
		List<Operation> ancestors = new ArrayList<>();

		for (Entity ancestor : (List<Entity>)entity.getAncestors())
			ancestors.add(createAncestorOperation((Node) ancestor));

		return ancestors;
	}

	private Operation createAncestorOperation(Node ancestor) {

		Operation operation = new ShowNodeOperation(new Context() {
			@Override
			public Presenter getCanvas() {
				return context.getCanvas();
			}

			@Override
			public cosmos.presenters.Operation getReferral() {
				return get();
			}
		}, ancestor, null);

		operation.inject(this.services);

		return operation;
	}

	private ShowNodeOperation get() {
		return this;
	}

	@Override
	public boolean disableButtonWhenExecute() {
		return true;
	}

	@Override
	public String getDefaultLabel() {
		if (entity.isEnvironment() && view != null)
			return view.getLabel();

		return entity.getLabel();
	}

	@Override
	public String getMenuLabel(boolean fullLabel) {
		if (fullLabel && view != null)
			return entity.getLabel() + " : " + view.getLabel();

		return getDefaultLabel();
	}

	@Override
	public String getDescription() {
		return entity.getDescription();
	}
}
