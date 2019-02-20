package org.monet.grided.control.actions.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.grided.core.model.Server;
import org.monet.grided.core.serializers.json.impl.JSONServersSerializer;
import org.monet.grided.core.services.grided.GridedService;

import com.google.inject.Inject;

public class LoadServersAction extends BaseAction {

    private JSONServersSerializer serializer;
    private GridedService gridedService;

    @Inject
    public LoadServersAction(JSONServersSerializer serializer, GridedService gridedService) {        
        this.serializer = serializer;
        this.gridedService = gridedService;
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        List<Server> servers = gridedService.loadServers();
        sendResponse(response, serializer.serialize(servers));
    }
}

