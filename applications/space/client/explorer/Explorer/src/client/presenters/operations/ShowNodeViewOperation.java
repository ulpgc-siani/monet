package client.presenters.operations;

import client.core.model.Node;
import client.core.model.NodeView;
import client.presenters.displays.EntityDisplay;
import client.presenters.displays.view.NodeViewDisplay;
import client.presenters.displays.view.ViewDisplay;
import cosmos.presenters.Presenter;

public class ShowNodeViewOperation extends ShowNodeOperation {

	public static final Type TYPE = new Type("ShowNodeViewOperation", Operation.TYPE);

	public ShowNodeViewOperation(Context context, Node node, NodeView view) {
		super(context, node, view);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public boolean disableButtonWhenExecute() {
		return true;
	}

	@Override
	protected void refresh(Node node) {
		refreshEntity(node);
		refreshCanvas(node);
	}

	@Override
	protected void refreshCanvas(Node node) {
		ViewDisplay viewDisplay = new NodeViewDisplay.Builder().build(node, view);
		Presenter display = context.getCanvas();

		if (display.existsChild(EntityDisplay.TYPE))
			display.removeChild(EntityDisplay.TYPE);

		display.addChild(viewDisplay);
		executePerformed();
	}

}
