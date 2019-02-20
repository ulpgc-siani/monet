package org.monet.grided.control.actions.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.monet.grided.constants.Actions;
import org.monet.grided.constants.ErrorCode;
import org.monet.grided.control.log.Logger;
import org.monet.grided.core.configuration.Configuration;
import org.monet.grided.core.constants.Params;
import org.monet.grided.core.lib.Filesystem;
import org.monet.grided.core.model.JSImage;
import org.monet.grided.core.serializers.json.impl.JSONImageSerializer;
import org.monet.grided.exceptions.DataException;
import org.monet.grided.library.LibraryFile;
import org.monet.grided.library.LibraryFileUploader;
import org.monet.grided.library.LibraryMultipartRequest;

import com.google.inject.Inject;

public class UploadImageAction extends BaseAction {

    private Configuration configuration;
    private Filesystem filesystem;
    private JSONImageSerializer serializer;
    private Logger logger;

    @Inject
    public UploadImageAction(Configuration configuration, Filesystem filesystem, JSONImageSerializer serializer, Logger logger) {
        this.configuration = configuration;
        this.filesystem = filesystem;
        this.serializer = serializer;
        this.logger = logger;
    }
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        File imageFile = null;
        LibraryMultipartRequest multipartRequest = new LibraryMultipartRequest(request);
        LibraryFileUploader uploader = new LibraryFileUploader();
                       
        FileItem fileItem   = multipartRequest.getFileParameter(Params.SOURCE);
        String serverId     = multipartRequest.getParameter(Params.SERVER_ID);
        String federationId = multipartRequest.getParameter(Params.FEDERATION_ID);
        String spaceId      = multipartRequest.getParameter(Params.SPACE_ID);
        String modelId      = multipartRequest.getParameter(Params.MODEL_ID);
        String modelType    = multipartRequest.getParameter(Params.MODEL_TYPE);
                
        String tempPath = configuration.getProperty(Configuration.TEMP_PATH);        
        File folder = new File(tempPath);
        String filename = UUID.randomUUID().toString() + "." + LibraryFile.getExtension(fileItem.getName());
        
        try {
            this.filesystem.removeDir(folder.getAbsolutePath());
            
            if (! this.filesystem.existFile(folder.getAbsolutePath())) {
                this.filesystem.mkdirs(folder.getAbsolutePath());
            }
            
            imageFile = uploader.uploadFile(fileItem, folder.getAbsolutePath(), filename);
            if (imageFile == null) throw new RuntimeException();
            
        } catch (Exception e) {            
            throw new DataException(ErrorCode.UPLOAD_IMAGE, e);            
        }   
        
        String imageURL = this.createResponse(request.getRequestURL().toString(), imageFile, serverId, federationId, spaceId, modelId, modelType);  
                
        sendResponse(response, this.serializer.serialize(new JSImage(filename, imageURL)));
    }

    private String createResponse(String url, File image, String serverId, String federationId, String spaceId, String modelId, String modelType) {
        String result = "";
        StringBuilder response = new StringBuilder();
        response.append(url).append("/?");
        response.append("op=" + Actions.DOWNLOAD_IMAGE).append("&");
        response.append(Params.FILENAME).append("=").append(image.getName()).append("&");
        response.append(Params.SERVER_ID).append("=").append(serverId).append("&");
        response.append(Params.FEDERATION_ID).append("=").append(federationId).append("&");
        response.append(Params.SPACE_ID).append(spaceId).append("&");
        response.append(Params.MODEL_ID).append(modelId).append("&");
        response.append(Params.MODEL_TYPE).append("=").append(modelType);
              
        try {
            result = URLEncoder.encode(response.toString(), Charset.forName("UTF-8").toString());
        } catch (UnsupportedEncodingException e) {           
            this.logger.error("Encoding URL to UTF-8", e);
        }
        
        return result;
    }
}

