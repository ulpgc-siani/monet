package org.monet.grided.control.actions.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.grided.core.constants.Params;
import org.monet.grided.core.model.Federation;
import org.monet.grided.core.serializers.json.impl.JSONFederationSerializer;
import org.monet.grided.core.services.deploy.DeployService;
import org.monet.grided.core.services.grided.GridedService;

import com.google.inject.Inject;

public class LoadFederationAction extends BaseAction {

    private GridedService service;
    private JSONFederationSerializer serializer;
//    private DeployService deployService;

    @Inject
    public LoadFederationAction(GridedService service, DeployService deployService, JSONFederationSerializer serializer) {
        this.service = service;
//        this.deployService = deployService;
        this.serializer = serializer;        
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {       
        String id       = request.getParameter(Params.ID);        
        Federation federation = this.service.loadFederation(Long.valueOf(id));

        //TODO call DeployService
        federation.start();
        
//        if (deployService.isFederationRunning(federation.getServer().getIp(), federation.getName())) { 
//            federation.setState(State.running);
//        }
//        else {
//            federation.setState(State.stopped);
//        }
         
        sendResponse(response, this.serializer.serialize(federation));
    }
}

