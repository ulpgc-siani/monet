package org.monet.grided.control.actions.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.grided.core.constants.Params;
import org.monet.grided.core.model.Space;
import org.monet.grided.core.serializers.json.impl.JSONSpaceSerializer;
import org.monet.grided.core.services.deploy.DeployService;
import org.monet.grided.core.services.grided.GridedService;

import com.google.inject.Inject;

public class StopSpaceAction extends BaseAction {

    private GridedService gridedService;
    private DeployService deployService;
    private JSONSpaceSerializer serializer;

    @Inject
    public StopSpaceAction(GridedService gridedService, DeployService deployService, JSONSpaceSerializer serializer) {
        this.gridedService = gridedService;
        this.deployService = deployService;
        this.serializer    = serializer;
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String spaceId = request.getParameter(Params.ID);
        Space space = this.gridedService.loadSpace(Long.valueOf(spaceId));
        
        // call deloyService
        //boolean result = this.deployService.stopSpace(Space.getFederation().getServer().getIp(), space.getName());
       
        space.stop();       
        this.gridedService.saveSpace(space);
        
        sendResponse(response, this.serializer.serialize(space));               
    }
}
