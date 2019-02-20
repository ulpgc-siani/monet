package org.monet.grided.control.actions.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.grided.core.constants.Params;
import org.monet.grided.core.model.Server;
import org.monet.grided.core.serializers.json.impl.JSONServerSerializer;
import org.monet.grided.core.serializers.json.impl.JSONSuccessResponse;
import org.monet.grided.core.services.deploy.DeployService;
import org.monet.grided.core.services.grided.GridedService;

import com.google.inject.Inject;

public class SaveServerAction extends BaseAction {

    private GridedService gridedService;
    private JSONServerSerializer serializer;
    private DeployService deployService;

    @Inject
    public SaveServerAction(GridedService gridedService, DeployService deployService, JSONServerSerializer serializer) {
        this.gridedService = gridedService;
        this.deployService = deployService;
        this.serializer = serializer;
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String jsonServer = request.getParameter(Params.SERVER);
        Server server = this.serializer.unserialize(jsonServer);
                
        this.gridedService.save(server);

        sendResponse(response, JSONSuccessResponse.OK);
    }
}

