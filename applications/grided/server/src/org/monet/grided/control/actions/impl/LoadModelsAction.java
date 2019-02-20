package org.monet.grided.control.actions.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.grided.core.model.Model;
import org.monet.grided.core.serializers.json.impl.JSONModelsSerializer;
import org.monet.grided.core.services.grided.GridedService;

import com.google.inject.Inject;

public class LoadModelsAction extends BaseAction {

    private GridedService service;
    private JSONModelsSerializer serializer;

    @Inject
    public LoadModelsAction(GridedService service, JSONModelsSerializer serializer) {
        this.service = service;
        this.serializer = serializer;
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        List<Model> models = this.service.loadModels();
        sendResponse(response, this.serializer.serialize(models));
    }
}

