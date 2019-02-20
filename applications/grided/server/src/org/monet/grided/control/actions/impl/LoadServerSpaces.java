package org.monet.grided.control.actions.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.grided.core.constants.Params;
import org.monet.grided.core.model.Space;
import org.monet.grided.core.serializers.json.impl.JSONSpacesSerializer;
import org.monet.grided.core.services.grided.GridedService;

import com.google.inject.Inject;

public class LoadServerSpaces extends BaseAction {

    private GridedService gridedService;
    private JSONSpacesSerializer serializer;    
    
    @Inject
    public LoadServerSpaces(GridedService gridedService, JSONSpacesSerializer serializer) {
        this.gridedService = gridedService;        
        this.serializer = serializer;
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        long serverId = Long.valueOf(request.getParameter(Params.ID));
        
        List<Space> spaces = this.gridedService.loadServerSpaces(serverId);               
        sendResponse(response, this.serializer.serialize(spaces));
    }
}

