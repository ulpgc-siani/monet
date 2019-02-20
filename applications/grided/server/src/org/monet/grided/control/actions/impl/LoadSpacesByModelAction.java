package org.monet.grided.control.actions.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.grided.core.constants.Params;
import org.monet.grided.core.model.Space;
import org.monet.grided.core.serializers.json.impl.JSONSpacesSerializer;
import org.monet.grided.core.services.grided.GridedService;

import com.google.inject.Inject;

public class LoadSpacesByModelAction extends BaseAction {

    private GridedService gridedService;    
    private JSONSpacesSerializer serializer;

    @Inject
    public LoadSpacesByModelAction(GridedService gridedService, JSONSpacesSerializer serializer) {
        this.gridedService = gridedService;
        this.serializer    = serializer;
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String modelId   = request.getParameter(Params.MODEL_ID);
        List<Space> spaces = this.gridedService.loadSpacesWithModel(Long.valueOf(modelId));
        sendResponse(response, this.serializer.serialize(spaces));
    }
}