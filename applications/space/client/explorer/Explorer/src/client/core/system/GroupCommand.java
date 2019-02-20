package client.core.system;

public class GroupCommand extends Command implements client.core.model.GroupCommand {
	private client.core.model.List<client.core.model.Command> commandList;

	public GroupCommand() {
	}

	public GroupCommand(Key key, String label, boolean enabled, client.core.model.List<client.core.model.Command> commandList) {
		super(key, label, enabled);
		this.commandList = commandList;
	}

	@Override
	public client.core.model.List<client.core.model.Command> getCommandList() {
		return this.commandList;
	}

	public void setCommandList(client.core.model.List<client.core.model.Command> commandList) {
		this.commandList = commandList;
	}

}
