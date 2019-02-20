package org.monet.grided.control.actions.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.monet.grided.agents.AgentImportMonitor;
import org.monet.grided.constants.ErrorCode;
import org.monet.grided.core.configuration.Configuration;
import org.monet.grided.core.constants.Params;
import org.monet.grided.core.serializers.json.impl.JSONErrorResponse;
import org.monet.grided.core.serializers.json.impl.JSONSuccessResponse;
import org.monet.grided.core.services.deploy.DeployService;
import org.monet.grided.library.LibraryFileUploader;
import org.monet.grided.library.LibraryMultipartRequest;

import com.google.inject.Inject;

public class StartImportAction extends BaseAction {

    private DeployService deployService;
    private Configuration configuration;

    @Inject
    public StartImportAction(DeployService deployService, Configuration configuration) {
        this.deployService = deployService;
        this.configuration = configuration;
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        
        LibraryMultipartRequest multipartRequest = new LibraryMultipartRequest(request);
        LibraryFileUploader uploader = new LibraryFileUploader();
                
        String spaceId        = multipartRequest.getParameter(Params.SPACE_ID);
        String importerTypeId = multipartRequest.getParameter(Params.IMPORTER_TYPE_ID);
        FileItem fileItem     = multipartRequest.getFileParameter(Params.SOURCE);
        
        try {
            String uploadDirectory = configuration.getProperty(Configuration.TEMP_PATH); 
            uploader.uploadFile(fileItem, uploadDirectory, fileItem.getName());
            AgentImportMonitor monitor = AgentImportMonitor.getInstance();
            monitor.startListening();
            //TODO call import service passing callback

        } catch (Exception e) {
            JSONErrorResponse error = new JSONErrorResponse(ErrorCode.IMPORT_DATA);
            sendResponse(response, error);
            return;
        }
        
        sendResponse(response, JSONSuccessResponse.OK);
    }
}

