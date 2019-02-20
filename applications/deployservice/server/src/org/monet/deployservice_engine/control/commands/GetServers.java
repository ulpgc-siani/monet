package org.monet.deployservice_engine.control.commands;

import org.monet.deployservice.xml.Item;
import org.monet.deployservice_engine.configuration.Configuration;
import org.monet.deployservice_engine.xml.ServersXML;


public class GetServers implements ICommand {
	
	public Item execute(Item command) {
    Item servers = null;
    ServersXML serversXML = new ServersXML();
    servers = serversXML.unserialize(Configuration.getServersConfig());
    String xmlServers = serversXML.serializeVirtual(servers);    

    Item result = new Item();
    result.setProperty("id", OperationIDs.GetServers);
    Item content = new Item();
    content.setProperty("id", "content");
    content.setContent(xmlServers);
		Item status = new Item();
		status.setProperty("id", "status");
		status.setContent("ok");
    
    result.addItem(status);    
    result.addItem(content);    
        
		return result;
	}
}
