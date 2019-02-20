package org.monet.deployservice_engine.control.commands;

import org.monet.deployservice.xml.Item;

public interface ICommand {	
	public Item execute(Item command);
}
