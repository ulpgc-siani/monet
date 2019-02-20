package org.monet.grided.control.actions.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.grided.core.configuration.Configuration;
import org.monet.grided.core.constants.Params;
import org.monet.grided.core.lib.Filesystem;
import org.monet.grided.core.model.Image;
import org.monet.grided.core.model.Model;
import org.monet.grided.core.model.ModelVersion;
import org.monet.grided.core.model.Space;
import org.monet.grided.core.serializers.json.impl.JSONErrorResponse;
import org.monet.grided.core.serializers.json.impl.JSONModelSerializer;
import org.monet.grided.core.serializers.json.impl.JSONSuccessResponse;
import org.monet.grided.core.services.grided.GridedService;
import org.monet.grided.core.services.space.SpaceService;

import com.google.inject.Inject;

public class SaveModelAction extends BaseAction {

    private GridedService service;
    private SpaceService spaceService;
    private Configuration configuration;
    private Filesystem filesystem;
    private JSONModelSerializer serializer;

    @Inject
    public SaveModelAction(GridedService service, SpaceService spaceService, Configuration configuration, Filesystem filesystem, JSONModelSerializer serializer) {
        this.service = service;
        this.spaceService = spaceService;
        
        this.configuration = configuration;
        this.filesystem = filesystem;        
        this.serializer = serializer;
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {      
        String jsonModel = request.getParameter(Params.MODEL);

        Model model = this.serializer.unserialize(jsonModel);

        try {
            this.saveModel(model);        
            this.updateModelInSpaces(model);
        } catch (Exception ex) {
            sendResponse(response, JSONErrorResponse.ERROR);
            return;
        }
        
        sendResponse(response, JSONSuccessResponse.OK);        
    }    
    
    
    private void saveModel(Model model) throws Exception {        
      //String logoFilename = model.getData().getLogoFilename();
        String logoFilename = "";
                
        String tempPath = this.configuration.getProperty(Configuration.TEMP_PATH);        
        File file = new File(tempPath + Filesystem.FILE_SEPARATOR + logoFilename);
        
        try {
            if (file.exists() && logoFilename.length() > 0) {
                File renamedFile = new File(file.getParent() + "/" + logoFilename); 
                file.renameTo(renamedFile);
                
                Image image = new Image(logoFilename, renamedFile);
                this.service.saveModel(model, image);
                this.filesystem.removeFile(renamedFile.getAbsolutePath());
            }
            else {
                this.service.saveModel(model);
            }
        } catch (Exception ex) {
            if (file.exists()) this.filesystem.removeFile(file.getAbsolutePath());
            throw ex;            
        }
    }
    
    
    private void updateModelInSpaces(Model model) {
        for (ModelVersion version : model.getVersions()) {            
            for (Space lazySpace : version.getSpaces()) {
                Space space = this.service.loadSpace(lazySpace.getId());                
                String spaceVersion = this.spaceService.getVersion(space.getData().getUrl());
                
                if (! version.getMetaModelVersion().isCompatible(spaceVersion)) {
                    byte[] bytes = this.service.loadVersionFile(model.getId(), version.getId());
                    InputStream stream = new ByteArrayInputStream(bytes);                   
                    this.spaceService.updateModel(space.getData().getUrl(), stream);
                }
            }
        }        
    }
}

