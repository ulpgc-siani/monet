package org.monet.grided.control.actions.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.grided.core.constants.Params;
import org.monet.grided.core.model.Federation;
import org.monet.grided.core.serializers.json.impl.JSONErrorResponse;
import org.monet.grided.core.serializers.json.impl.JSONFederationSerializer;
import org.monet.grided.core.services.deploy.DeployService;
import org.monet.grided.core.services.grided.GridedService;

import com.google.inject.Inject;

public class AddFederationAction extends BaseAction {

    private GridedService gridedService;
    private JSONFederationSerializer serializer;
//    private DeployService deployService;

    @Inject
    public AddFederationAction(GridedService gridedService, DeployService deployService, JSONFederationSerializer serializer) {
        this.gridedService = gridedService;
//        this.deployService = deployService;
        this.serializer = serializer;
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        Federation federation = null;
        String serverId = request.getParameter(Params.SERVER_ID);
        String name     = request.getParameter(Params.NAME);
        String url      = request.getParameter(Params.URL);
        
        try {
            
            federation = this.gridedService.createFederation(Long.parseLong(serverId), name, url);
                        
        } catch (Exception ex) {
            sendResponse(response, JSONErrorResponse.ERROR);
            return;
        }

        sendResponse(response, this.serializer.serialize(federation));
    }
}