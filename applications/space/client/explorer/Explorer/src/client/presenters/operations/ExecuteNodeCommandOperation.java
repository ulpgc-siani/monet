package client.presenters.operations;

import client.core.messages.ExecutingNodeCommandError;
import client.core.model.*;
import client.services.NodeService;
import client.services.callback.NodeCommandCallback;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ExecuteNodeCommandOperation extends Operation implements EntityOperation<Node,NodeView> {
	private final Command command;
	private final Node entity;

	public static final Type TYPE = new Type("ExecuteNodeCommandOperation", Operation.TYPE);

	public ExecuteNodeCommandOperation(cosmos.presenters.Operation.Context context, Command command, Node node) {
		super(context);
		this.command = command;
		this.entity = node;
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void doExecute() {
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, this.getType().toString() + " called. Command: " + command.getLabel() + " - Node: " + entity.getLabel());

		getMessageDisplay().showLoading(services.getTranslatorService().getLoadingLabel());
		NodeService service = this.services.getNodeService();
		service.executeCommand(entity, command, new NodeCommandCallback() {
			@Override
			public void success(Space.Action action) {
				new ActionDispatcher(services).dispatch(action, context);
				getMessageDisplay().hideLoading();
			}

			@Override
			public void failure(String error) {
				getMessageDisplay().hideLoading();
				executeFailed(new ExecutingNodeCommandError(error));
			}
		});
	}

	@Override
	public boolean disableButtonWhenExecute() {
		return true;
	}

	@Override
	public Node getEntity() {
		return entity;
	}

	@Override
	public NodeView getView() {
		return null;
	}

	@Override
	public String getDescription() {
		return entity.getDescription();
	}

	@Override
	public String getDefaultLabel() {
		return command.getLabel();
	}

	@Override
	public String getName() {
		Key key = command.getKey();
		return super.getName() + "-" + (key.getName() != null ? key.getName() : key.getCode());
	}
}
