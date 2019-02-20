package org.monet.grided.control.actions.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.grided.core.constants.Params;
import org.monet.grided.core.model.Server;
import org.monet.grided.core.serializers.json.impl.JSONServerSerializer;
import org.monet.grided.core.services.deploy.DeployService;
import org.monet.grided.core.services.grided.GridedService;

import com.google.inject.Inject;

public class AddServerAction extends BaseAction {

    private GridedService gridedService;
    private JSONServerSerializer serializer;
//    private DeployService deployService;

    @Inject
    public AddServerAction(GridedService gridedService, DeployService deployService, JSONServerSerializer serializer) {
        this.gridedService = gridedService;
//        this.deployService = deployService;
        this.serializer = serializer;
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {        
        String name = request.getParameter(Params.NAME);
        String ip   = request.getParameter(Params.IP);
        
        //TODO call deployService
        boolean enabled = true;
//        if (deployService.ping(ip)) {
//            enabled = true;
//        }          
        
        Server server = this.gridedService.createServer(name, ip, enabled);
        sendResponse(response, this.serializer.serialize(server));
    }
}

