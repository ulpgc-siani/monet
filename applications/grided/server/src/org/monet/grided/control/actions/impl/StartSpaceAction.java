package org.monet.grided.control.actions.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.grided.core.constants.Params;
import org.monet.grided.core.model.Space;
import org.monet.grided.core.serializers.json.impl.JSONSpaceSerializer;
import org.monet.grided.core.services.deploy.DeployService;
import org.monet.grided.core.services.grided.GridedService;

import com.google.inject.Inject;

public class StartSpaceAction extends BaseAction {

    private GridedService gridedService;
    private DeployService deployService;
    private JSONSpaceSerializer serializer;

    @Inject
    public StartSpaceAction(GridedService gridedService, DeployService deployService, JSONSpaceSerializer serializer) {
        this.gridedService = gridedService;
        this.deployService = deployService;
        this.serializer    = serializer;
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String spaceId = request.getParameter(Params.ID);
        Space space = this.gridedService.loadSpace(Long.valueOf(spaceId));
        
        //TODO call deploy service
        //boolean result = this.deployService.startSpace(space.getFederation().getServer().getIp(), space.getName());
        
        space.start();
        this.gridedService.saveSpace(space);
        
        sendResponse(response, this.serializer.serialize(space));               
    }
}

