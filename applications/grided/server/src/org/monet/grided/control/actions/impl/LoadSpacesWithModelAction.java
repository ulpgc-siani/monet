package org.monet.grided.control.actions.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.grided.core.constants.Params;
import org.monet.grided.core.model.Model;
import org.monet.grided.core.model.ModelVersion;
import org.monet.grided.core.model.Space;
import org.monet.grided.core.model.SpaceVersion;
import org.monet.grided.core.serializers.json.impl.JSONSpacesSerializer;
import org.monet.grided.core.services.grided.GridedService;
import org.monet.grided.core.services.space.SpaceService;

import com.google.inject.Inject;

public class LoadSpacesWithModelAction extends BaseAction {

    private GridedService gridedService;
    private SpaceService spaceService;
    private JSONSpacesSerializer serializer;

    @Inject
    public LoadSpacesWithModelAction(GridedService gridedService, SpaceService spaceService, JSONSpacesSerializer serializer) {
        this.gridedService = gridedService;
        this.spaceService = spaceService;
        this.serializer = serializer;
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        List<Space> filteredSpaces = new ArrayList<Space>();
        
        String modelId   = request.getParameter(Params.MODEL_ID);
        String versionId = request.getParameter(Params.VERSION_ID);
        
        Model model        = this.gridedService.loadModel(Long.valueOf(modelId));
//        List<Space> spaces = this.gridedService.loadSpacesWithModel(Long.valueOf(modelId));
//        ModelVersion modelVersion = model.getVersionById(Long.valueOf(versionId)); 
//                
//        for (Space space : spaces) {
//            SpaceVersion spaceVersion = new SpaceVersion(this.spaceService.getVersion(space.getData().getUrl()));
//            
//            if (spaceVersion.isCompatible(modelVersion.getMetaModelVersion().getValue())) {
//                filteredSpaces.add(space);
//            }                
//        }
        sendResponse(response, this.serializer.serialize(filteredSpaces));
    }
}

