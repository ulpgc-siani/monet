package client.presenters.operations;

import client.core.messages.CreatingNodeError;
import client.core.model.Node;
import client.services.callback.NodeCallback;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AddNodeOperation extends Operation {
	private Operation redirection;
	private Node parent;
	private String code;
	private Node result = null;

	public static final Type TYPE = new Type("AddNodeOperation", Operation.TYPE);

	public AddNodeOperation(Context context, Node parent, String code, Operation redirection) {
		super(context);
		this.parent = parent;
		this.code = code;
		this.redirection = redirection;
	}

	public AddNodeOperation(Context context, Node parent, String code) {
		this(context, parent, code, null);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	public Node getNode() {
		return result;
	}

	@Override
	public void doExecute() {
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, this.getType().toString() + " called. Parent: " + this.parent.getLabel() + ". Type: " + this.code + ".");
		getMessageDisplay().showLoading(services.getTranslatorService().getLoadingLabel());

		services.getNodeService().add(parent, code, new NodeCallback() {
			@Override
			public void success(Node node) {
				getMessageDisplay().hideLoading();
				AddNodeOperation.this.result = node;
				redirectToNode();
				executePerformed();
			}

			@Override
			public void failure(String details) {
				getMessageDisplay().hideLoading();
				executeFailed(new CreatingNodeError(details));
			}
		});
	}

	@Override
	public boolean disableButtonWhenExecute() {
		return true;
	}

	@Override
	public String getDefaultLabel() {
		return services.getSpaceService().load().getDictionary().getDefinitionLabel(code);
	}

	public void setRedirection(Operation redirection) {
		this.redirection = redirection;
	}

	private void redirectToNode() {
		if (redirection != null) {
			redirection.execute();
			return;
		}

		ShowNodeOperation operation = new ShowNodeOperation(context, result, null);
		operation.inject(services);
		operation.execute();
	}
}
