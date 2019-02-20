package org.monet.grided.control.actions.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.grided.core.model.Federation;
import org.monet.grided.core.serializers.json.impl.JSONFederationsSerializer;
import org.monet.grided.core.services.deploy.DeployService;
import org.monet.grided.core.services.grided.GridedService;

import com.google.inject.Inject;

public class LoadFederationsAction extends BaseAction {

    private GridedService gridedService;
    private JSONFederationsSerializer serializer;
//    private DeployService deployService;

    @Inject
    public LoadFederationsAction(GridedService gridedService, DeployService deployService, JSONFederationsSerializer serializer) {
        this.gridedService = gridedService;
//        this.deployService = deployService;
        this.serializer = serializer;
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        List<Federation> federations = this.gridedService.loadAllFederations();
/*        
        for (Federation federation : federations) {

            // CALL to DeployService
            //boolean value = true;
            //boolean value = deployService.isFederationRunning(federation.getServer().getIp(), federation.getName());
            //federation.setState((value)? State.running : State.stopped);
        }
*/        
        sendResponse(response, this.serializer.serialize(federations));
    }
}

