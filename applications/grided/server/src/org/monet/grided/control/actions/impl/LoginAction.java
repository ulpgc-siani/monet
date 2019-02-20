package org.monet.grided.control.actions.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.grided.core.model.Server;
import org.monet.grided.core.services.grided.GridedService;

import com.google.inject.Inject;


public class LoginAction extends BaseAction {
	
	private GridedService service;

    @Inject
	public LoginAction(GridedService service) {		
		this.service = service;
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
	    for (Server server : this.service.loadServers()) {
	        System.out.println(server);
	    }
		sendResponse(response, "<html>hello</html>");
	}
}

