package org.monet.grided.control.actions.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.grided.core.constants.Params;
import org.monet.grided.core.model.Space;
import org.monet.grided.core.serializers.json.impl.JSONSpaceSerializer;
import org.monet.grided.core.services.grided.GridedService;

import com.google.inject.Inject;

public class LoadSpaceAction extends BaseAction {

    private GridedService gridedService;
    private JSONSpaceSerializer serializer;

    @Inject
    public LoadSpaceAction(GridedService gridedService, JSONSpaceSerializer serializer) {
        this.gridedService = gridedService;
        this.serializer = serializer;
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter(Params.ID);
        Space space = this.gridedService.loadSpace(Long.valueOf(id));
        
        //TODO cargar la version desde el SpaceSetupApi
        
        sendResponse(response, this.serializer.serialize(space));
    }
}

