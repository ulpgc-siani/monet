package org.monet.deployservicemanager.security;

import java.io.IOException;

import org.monet.deployservicemanager.configuration.Configuration;
import org.monet.deployservicemanager.utils.Network;
import org.monet.deployservicemanager.xml.Item;
import org.monet.deployservicemanager.xml.ResultXML;

public class Security {

	public boolean verifyUser(String user, String password) throws IOException {
		if (user == null || password == null || user.equals("") || password.equals("")) return false;
		
		String command = "<command id='get_servers'>";
		command += "<parameter id='user'>" + user + "</parameter>";
		command += "<parameter id='password'>" + password + "</parameter>";
		command += "</command>";
		
		Network network = new Network(Configuration.getDomain(), Configuration.getPort());		
		String get_servers = network.SendTCP(command);
		
		ResultXML resultXML = new ResultXML();
		Item result = resultXML.unserializelite(get_servers);
							
		if (result.getItem("status").getContent().equals("ok")) 
			return true;
		else
			return false;				
	}
	
}
