package org.monet.grided.control.actions.impl;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.grided.core.configuration.Configuration;
import org.monet.grided.core.constants.Params;
import org.monet.grided.core.lib.Filesystem;
import org.monet.grided.core.model.Federation;
import org.monet.grided.core.model.Image;
import org.monet.grided.core.serializers.json.impl.JSONErrorResponse;
import org.monet.grided.core.serializers.json.impl.JSONFederationSerializer;
import org.monet.grided.core.serializers.json.impl.JSONSuccessResponse;
import org.monet.grided.core.services.grided.GridedService;

import com.google.inject.Inject;

public class SaveFederationAction extends BaseAction {

    private GridedService gridedService;
    private JSONFederationSerializer serializer;
    private Configuration configuration;
    private Filesystem filesystem;

    @Inject
    public SaveFederationAction(GridedService gridedService, Configuration configuration, Filesystem filesystem, JSONFederationSerializer serializer) {
        this.gridedService = gridedService;
        this.configuration = configuration;
        this.filesystem = filesystem;
        this.serializer = serializer;
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        
        String jsonFederation = request.getParameter(Params.FEDERATION);
        
        Federation federation = this.serializer.unserialize(jsonFederation);
        String logoFilename = federation.getData().getLogoFilename();
        
        String tempPath = this.configuration.getProperty(Configuration.TEMP_PATH);
        File file = new File(tempPath + Filesystem.FILE_SEPARATOR + logoFilename);        
        
        try {
            if (logoFilename.isEmpty() || ! file.exists()) {
                this.gridedService.saveFederation(federation);
            }
            else {
                Image image = new Image(logoFilename, file);
                this.gridedService.saveFederation(federation, image);
                this.filesystem.removeFile(file.getAbsolutePath());  
            }
            
        } catch (Exception ex) {
          if (file.exists()) this.filesystem.removeFile(file.getAbsolutePath());
          sendResponse(response, JSONErrorResponse.ERROR);
          return;            
        }
                
        sendResponse(response, JSONSuccessResponse.OK);
    }
}