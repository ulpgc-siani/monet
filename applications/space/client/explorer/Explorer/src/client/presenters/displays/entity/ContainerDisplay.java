package client.presenters.displays.entity;

import client.core.model.*;
import client.core.model.definition.entity.ContainerDefinition;
import cosmos.presenters.Presenter;

public class ContainerDisplay extends NodeDisplay<Container, ContainerView> implements EnvironmentDisplay {

	public static final Type TYPE = new Type("ContainerDisplay", EnvironmentDisplay.TYPE);

	public ContainerDisplay(Container container, ContainerView view) {
		super(container, view);
	}

	@Override
	protected void onInjectServices() {
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	protected void addUserCommandsAsOperations(Presenter presenter, List<Command> commandList) {
		super.addUserCommandsAsOperations(presenter, commandList);

		Node child = getView().getHostView().getScope();
		for (Command command : (List<Command>) child.getCommands())
			addUserCommandAsOperation(presenter, command);
	}

	@Override
	public boolean isRoot() {
        return isEnvironment() && getEntity().equals(services.getAccountService().load().getRootNode());

    }

	private boolean isEnvironment() {
        ContainerDefinition definition = (ContainerDefinition) getEntity().getDefinition();
		return definition != null && definition.isEnvironment();
	}

}
