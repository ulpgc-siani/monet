package org.monet.grided.control.actions.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.grided.core.constants.Params;
import org.monet.grided.core.model.Model;
import org.monet.grided.core.serializers.json.impl.JSONModelSerializer;
import org.monet.grided.core.services.grided.GridedService;

import com.google.inject.Inject;

public class LoadModelAction extends BaseAction {

    private GridedService service;
    private JSONModelSerializer serializer;

    @Inject
    public LoadModelAction(GridedService service, JSONModelSerializer serializer) {
        this.service = service;        
        this.serializer = serializer;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String modelId = request.getParameter(Params.ID);        
        Model model = this.service.loadModel(Long.valueOf(modelId));                                               
        sendResponse(response, this.serializer.serialize(model));
    }
}