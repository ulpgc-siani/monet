package client.core.model;

public interface GroupCommand extends Command {
	List<Command> getCommandList();
}
