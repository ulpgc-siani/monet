package org.monet.grided.control.actions.impl;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.grided.core.configuration.Configuration;
import org.monet.grided.core.constants.Params;
import org.monet.grided.core.lib.Filesystem;
import org.monet.grided.core.model.Image;
import org.monet.grided.core.model.Space;
import org.monet.grided.core.serializers.json.impl.JSONErrorResponse;
import org.monet.grided.core.serializers.json.impl.JSONSpaceSerializer;
import org.monet.grided.core.serializers.json.impl.JSONSuccessResponse;
import org.monet.grided.core.services.grided.GridedService;

import com.google.inject.Inject;

public class SaveSpaceAction extends BaseAction {
        
    private GridedService gridedService;
    private Configuration configuration;
    private JSONSpaceSerializer serializer;
    private Filesystem filesystem;

    @Inject
    public SaveSpaceAction(GridedService gridedService, Configuration configuration, Filesystem filesystem, JSONSpaceSerializer serializer) {
        this.gridedService = gridedService;
        this.serializer = serializer;
        this.filesystem = filesystem;
        this.configuration = configuration;
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        
        String jsonSpace = request.getParameter(Params.SPACE);
            
        Space space = this.serializer.unserialize(jsonSpace);        
        String logoFilename = space.getData().getLogoFilename();                                                 
                                
        String tempPath = this.configuration.getProperty(Configuration.TEMP_PATH);
        File file = new File(tempPath + Filesystem.FILE_SEPARATOR + logoFilename);
        
        try {                
            if (logoFilename.isEmpty() || ! file.exists()) {
                this.gridedService.saveSpace(space);
            }
            else {
                File renamedFile = new File(file.getParent() + "/" + logoFilename); 
                file.renameTo(renamedFile);

                Image image = new Image(logoFilename, renamedFile);
                this.gridedService.saveSpace(space, image);
                this.filesystem.removeFile(renamedFile.getAbsolutePath());
            }
               
        } catch (Exception ex) {
            if (file.exists()) this.filesystem.removeFile(file.getAbsolutePath());
            sendResponse(response, JSONErrorResponse.ERROR);
            return;
        }                
        sendResponse(response, JSONSuccessResponse.OK);
    }
}

