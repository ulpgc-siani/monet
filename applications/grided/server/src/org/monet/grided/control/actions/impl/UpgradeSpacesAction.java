package org.monet.grided.control.actions.impl;

import java.io.ByteArrayInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.monet.grided.core.constants.Params;
import org.monet.grided.core.model.Model;
import org.monet.grided.core.model.ModelVersion;
import org.monet.grided.core.model.Space;
import org.monet.grided.core.serializers.json.impl.JSONErrorResponse;
import org.monet.grided.core.serializers.json.impl.JSONSuccessResponse;
import org.monet.grided.core.services.grided.GridedService;
import org.monet.grided.core.services.space.SpaceService;

import com.google.inject.Inject;

public class UpgradeSpacesAction extends BaseAction {

    private GridedService gridedService;
    private SpaceService spaceService;

    @Inject
    public UpgradeSpacesAction(GridedService gridedService, SpaceService spaceService) {
        this.gridedService = gridedService;
        this.spaceService  = spaceService; 
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        
        String jsonIds   = request.getParameter(Params.IDS);
        String versionId = request.getParameter(Params.VERSION_ID); 
        String modelId   = request.getParameter(Params.MODEL_ID);
        
        try {
            
            long[] ids = this.getSpaceIds(jsonIds);            
            
            ModelVersion modelVersion = this.gridedService.loadModelVersion(Long.valueOf(modelId), Long.valueOf(versionId));
            Model model = this.gridedService.loadModel(Long.valueOf(modelId));
            ModelVersion nextModelVersion = model.getNextVersion(modelVersion);
            
            for (long id : ids) {                    
                Space space = this.gridedService.loadSpace(id);
                space.setModelVersion(nextModelVersion);
                this.gridedService.saveSpace(space);
                
                byte[] bytes = this.gridedService.loadVersionFile(model.getId(), nextModelVersion.getId());
                this.spaceService.updateModel(space.getData().getUrl(), new ByteArrayInputStream(bytes));                
            }     
            
        } catch (Exception ex) {
            sendResponse(response, JSONErrorResponse.ERROR);
            return;
        }
        
        sendResponse(response, JSONSuccessResponse.OK);
        
    }
    
    private long[] getSpaceIds(String jsonIds) {
        JSONArray jsonArray = JSONArray.fromObject(jsonIds);
        
        long[] ids = new long[jsonArray.size()];
        for (int i=0; i < jsonArray.size(); i++) {                
            ids[i] = Long.valueOf(jsonArray.getString(i)); 
        }        
        return ids;            
    }
}

