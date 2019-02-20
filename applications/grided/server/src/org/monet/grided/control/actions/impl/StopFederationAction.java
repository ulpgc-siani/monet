package org.monet.grided.control.actions.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.grided.core.constants.Params;
import org.monet.grided.core.model.Federation;
import org.monet.grided.core.serializers.json.impl.JSONFederationSerializer;
import org.monet.grided.core.services.deploy.DeployService;
import org.monet.grided.core.services.grided.GridedService;

import com.google.inject.Inject;

public class StopFederationAction extends BaseAction {

    private GridedService gridedService;
    private DeployService deployService;
    private JSONFederationSerializer serializer;

    @Inject
    public StopFederationAction(GridedService gridedService, DeployService deployService, JSONFederationSerializer serializer) {
        this.gridedService = gridedService;
        this.deployService = deployService;
        this.serializer = serializer;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String federationId = request.getParameter(Params.ID);
        Federation federation = this.gridedService.loadFederation(Long.valueOf(federationId));
        
        // call deloyService
        //boolean result = this.deployService.stopFederation(federation.getServer().getIp(), federation.getName());
        
        federation.stop();
        this.gridedService.saveFederation(federation);
        
        sendResponse(response, this.serializer.serialize(federation));               
    }
}

