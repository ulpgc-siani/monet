package org.monet.grided.control.actions.impl;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.monet.grided.constants.ErrorCode;
import org.monet.grided.core.configuration.Configuration;
import org.monet.grided.core.constants.Params;
import org.monet.grided.core.lib.Filesystem;
import org.monet.grided.core.model.ModelVersion;
import org.monet.grided.core.persistence.exceptions.DuplicateModelVersionException;
import org.monet.grided.core.serializers.json.impl.JSONErrorResponse;
import org.monet.grided.core.serializers.json.impl.JSONModelVersionSerializer;
import org.monet.grided.core.services.grided.GridedService;
import org.monet.grided.library.LibraryFileUploader;
import org.monet.grided.library.LibraryMultipartRequest;

import com.google.inject.Inject;

public class UploadModelVersionAction extends BaseAction {

    private Configuration configuration;
    private Filesystem filesystem;
    private GridedService gridedService;
    private JSONModelVersionSerializer serializer;

    @Inject
    public UploadModelVersionAction(Configuration configuration, Filesystem filesystem, GridedService gridedService, JSONModelVersionSerializer serializer) {
        this.configuration = configuration;
        this.filesystem = filesystem;
        this.gridedService = gridedService;  
        this.serializer = serializer;
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        ModelVersion modelVersion = null;
        
        LibraryMultipartRequest multipartRequest = new LibraryMultipartRequest(request);
        LibraryFileUploader uploader = new LibraryFileUploader();
                       
        FileItem fileItem  = multipartRequest.getFileParameter(Params.SOURCE);                
        String   modelId   = multipartRequest.getParameter(Params.MODEL_ID);
        
        String uploadDirectory = configuration.getProperty(Configuration.TEMP_PATH);        
        
        try {                       
            File versionFile = uploader.uploadFile(fileItem, uploadDirectory, fileItem.getName());
            modelVersion = this.gridedService.saveModelVersion(Long.valueOf(modelId), versionFile);                        
            
        } catch(DuplicateModelVersionException ex) {
            JSONErrorResponse error = new JSONErrorResponse(ErrorCode.DUPLICATE_MODEL_VERSION);
            sendResponse(response, error);
            return;
            
        } catch( Exception ex) {
            sendResponse(response, JSONErrorResponse.ERROR);
            return;
        } finally {
            String filename = uploadDirectory + Filesystem.FILE_SEPARATOR + fileItem.getName();
            this.filesystem.removeFile(filename);
        }
                
        sendResponse(response, this.serializer.serialize(modelVersion));
    }
}
