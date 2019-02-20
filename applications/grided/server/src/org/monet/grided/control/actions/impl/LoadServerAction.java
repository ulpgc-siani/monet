package org.monet.grided.control.actions.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.grided.core.constants.Params;
import org.monet.grided.core.model.Server;
import org.monet.grided.core.serializers.json.impl.JSONServerSerializer;
import org.monet.grided.core.services.deploy.DeployService;
import org.monet.grided.core.services.grided.GridedService;

import com.google.inject.Inject;

public class LoadServerAction extends BaseAction {

    private JSONServerSerializer serializer;
    private GridedService service;
    private DeployService deployService;

    @Inject
    public LoadServerAction(JSONServerSerializer serializer, GridedService service, DeployService deployService) {
        this.serializer = serializer;
        this.service = service;
        this.deployService = deployService;
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        long serverId = Long.valueOf(request.getParameter(Params.ID));
        Server server = this.service.loadServer(serverId);
        server.setState(this.deployService.getServerState(server.getIp()));
        
        sendResponse(response, serializer.serialize(server));
    }
}

