package client.presenters.operations;

import client.core.model.Node;
import client.core.model.NodeView;
import client.presenters.displays.ApplicationDisplay;
import client.presenters.displays.EntityDisplay;
import client.presenters.displays.EntityVisitingDisplay;
import client.presenters.displays.ExplorationDisplay;
import client.presenters.displays.entity.NodeDisplay;

public class ShowEnvironmentOperation extends ShowNodeOperation {

	public static final Type TYPE = new Type("ShowEnvironmentOperation", Operation.TYPE);

	public ShowEnvironmentOperation(Context context, Node node, NodeView view) {
		super(context, node, view);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	protected void refreshExplorationDisplay() {
		ExplorationDisplay explorationDisplay = context.<ApplicationDisplay>getCanvas().getExplorationDisplay();

		explorationDisplay.clearChildren();
		explorationDisplay.setRootOperation(explorationDisplay.get(this));
		explorationDisplay.activate(this);
	}

	@Override
	protected void refreshCanvas(Node node) {
		EntityDisplay entityDisplay = new NodeDisplay.Builder().build(node, view);
		EntityVisitingDisplay visitingDisplay = new EntityVisitingDisplay(entityDisplay, context.getReferral());
		ApplicationDisplay display = context.getCanvas();

		display.removeVisitingDisplay();
		display.addVisitingDisplay(visitingDisplay);

		this.executePerformed();
	}
}
