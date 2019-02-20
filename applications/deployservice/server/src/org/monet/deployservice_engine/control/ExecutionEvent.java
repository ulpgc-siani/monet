package org.monet.deployservice_engine.control;

import org.monet.deployservice_engine.control.commands.IAsynchronousCommand;

public class ExecutionEvent {
	
	public static final int COMMAND_STARTED  = 0;
	public static final int COMMAND_FINISHED = 1;
	public IAsynchronousCommand command;
	public int      eventType;
	
	public ExecutionEvent(int eventType, IAsynchronousCommand command) {
		this.eventType = eventType;
		this.command   = command;
	}
	
	public int getType() {
		return this.eventType;
	}
	
	public IAsynchronousCommand getCommand() {
		return this.command;
	}

}
