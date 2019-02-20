package org.monet.deployservice_engine.control.commands;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class Factory {
	
  private Map<String, Class> commandsTable;
	private static Factory instance;
	
	public static Factory getInstance() {
		if (instance == null) instance = new Factory();
		
		
		return instance;		
	}
	
	private Factory() {
		commandsTable = new HashMap<String, Class>();
	}
	
	public void register(String op, Class commandClass) {
		commandsTable.put(op, commandClass);
	}

	public void unRegister(String op) {
		commandsTable.remove(op);
	}
	
	public void clear() {
		commandsTable.clear();
	}
	
	public ICommand getCommand(String op) {
		ICommand command = null;
		try {
			Class commandClass = commandsTable.get(op);
			command = (ICommand) commandClass.newInstance();
			if (command instanceof IAsynchronousCommand) {			
				((IAsynchronousCommand)command).setState(IAsynchronousCommand.ILDE);
			}
		} catch(Exception ex) {
			throw new RuntimeException("The command is not exists.");
		}
		return command;
	}
	
	public void dispose() {
		commandsTable.clear();
		commandsTable = null;		
	}
}