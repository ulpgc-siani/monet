package org.monet.grided.control.actions.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;
import org.monet.grided.constants.ErrorCode;
import org.monet.grided.constants.MimeTypes;
import org.monet.grided.control.log.Logger;
import org.monet.grided.core.configuration.Configuration;
import org.monet.grided.core.constants.Params;
import org.monet.grided.core.lib.Filesystem;
import org.monet.grided.core.services.grided.GridedService;
import org.monet.grided.library.LibraryFile;

import com.google.inject.Inject;

public class DownloadImageAction extends BaseAction {

    private Configuration configuration;
    private Filesystem filesystem;
    private GridedService gridedService;
    private Logger logger;

    @Inject
    public DownloadImageAction(Configuration configuration, Filesystem filesystem, GridedService gridedService, Logger logger) {
        this.configuration = configuration;
        this.filesystem = filesystem;
        this.gridedService = gridedService;
        this.logger = logger;
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {                
        String filename     = request.getParameter(Params.FILENAME);        
        String serverId     = request.getParameter(Params.SERVER_ID);
        String federationId = request.getParameter(Params.FEDERATION_ID);
        String spaceId      = request.getParameter(Params.SPACE_ID);
        String modelId      = request.getParameter(Params.MODEL_ID); 
        String modelType    = request.getParameter(Params.MODEL_TYPE);
        
        String tempPath = this.configuration.getProperty(Configuration.TEMP_PATH);

        try {
        
            byte[] image = new byte[0];
            String codeFormat = MimeTypes.getInstance().get(LibraryFile.getExtension(filename));
            
            if (! filename.isEmpty() && this.filesystem.existFile(tempPath + Filesystem.FILE_SEPARATOR + filename)) {
                image = this.filesystem.getBytesFromFile(tempPath + Filesystem.FILE_SEPARATOR + filename);
            } 
            else {        
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put(Params.FILENAME, filename);
                parameters.put(Params.SERVER_ID, serverId);
                parameters.put(Params.FEDERATION_ID, federationId);
                parameters.put(Params.SPACE_ID, spaceId);
                parameters.put(Params.MODEL_ID, modelId);
                parameters.put(Params.MODEL_TYPE, modelType);
                                
                image = this.gridedService.loadImage(parameters);                
            }                       
                        
            response.setContentType(codeFormat);
            response.setHeader("Content-Disposition", "inline;");
            response.getOutputStream().write(image);
            response.getOutputStream().flush();     
        }
        catch (Exception ex) {
            this.logger.error(ErrorCode.DOWNLOAD_IMAGE, ex);
            try { response.sendError(Response.SC_NO_CONTENT); } catch (IOException e) { e.printStackTrace(); }
        }                
    }
}
