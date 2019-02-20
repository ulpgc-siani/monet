package client.presenters.displays.view;

import client.core.model.*;
import client.presenters.operations.EntityOperation;
import client.presenters.operations.Operation;
import client.presenters.operations.ShowDashboardOperation;
import client.presenters.operations.ShowNodeOperation;
import cosmos.presenters.Presenter;

import java.util.ArrayList;
import java.util.List;

import static cosmos.presenters.Operation.Context;

public class DesktopViewDisplay extends NodeViewDisplay<DesktopView> {

	public static final Type TYPE = new Type("DesktopViewDisplay", NodeViewDisplay.TYPE);

	public DesktopViewDisplay(Node node, DesktopView nodeView) {
        super(node, nodeView);
    }

	@Override
	protected void onInjectServices() {
		if (getView() instanceof ProxyView)
			return;

		addShows();
	}

	public EntityOperation[] getOperations() {
		List<Operation> result = new ArrayList<>();

		for (Object child : this) {
			if (child instanceof Operation)
				result.add((Operation)child);
		}

		return result.toArray(new EntityOperation[result.size()]);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	private void addShows() {
		DesktopView desktopView = getView();

		for (Entity entity : desktopView.getShows()) {

			Operation operation = createOperation(entity);
			if (operation == null)
				continue;

			operation.inject(services);

			this.addChild(operation);
		}
	}

	private Operation createOperation(Entity entity) {
		Operation operation = null;

		if (entity instanceof Node)
			return createNodeOperation((Node)entity);

		if (entity instanceof Dashboard)
			return createDashboardOperation((Dashboard) entity);

		if (operation != null)
			operation.inject(services);

		return operation;
	}

	private Operation createNodeOperation(Node entity) {
		return new ShowNodeOperation(new Context() {
			@Override
			public Presenter getCanvas() {
				return DesktopViewDisplay.this.getRootDisplay();
			}
			@Override
			public cosmos.presenters.Operation getReferral() {
				return getVisitingDisplayOperation();
			}
		}, entity, null);
	}

	private Operation createDashboardOperation(Dashboard entity) {
		return new ShowDashboardOperation(new Context() {
			@Override
			public Presenter getCanvas() {
				return DesktopViewDisplay.this.getOwner().getOwner();
			}
			@Override
			public cosmos.presenters.Operation getReferral() {
				return getVisitingDisplayOperation();
			}
		}, entity, null);
	}

	public static class Builder extends ViewDisplay.Builder<Desktop, DesktopView> {

		protected static void register() {
			registerBuilder(Desktop.CLASS_NAME.toString() + DesktopView.CLASS_NAME.toString(), new Builder());
		}

		@Override
		public ViewDisplay build(Desktop entity, DesktopView view) {
			return new DesktopViewDisplay(entity, view);
		}
	}

}
