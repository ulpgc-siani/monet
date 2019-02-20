package org.monet.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.monet.filesystem.StreamHelper;

public class LibraryRequest {
	
	  @SuppressWarnings("unchecked")
    private static void parseMultipartParameters(HttpServletRequest request, HashMap<String, Object> requestParameters) {
	    FileItemFactory factory = new DiskFileItemFactory();
	    List<FileItem> list = new LinkedList<FileItem>();
	    ServletFileUpload uploadsManager = new ServletFileUpload(factory);
	    Iterator<FileItem> iterator;

	    try {
	      request.setCharacterEncoding("UTF-8");
	    } catch (Exception e) {}

	    if (!ServletFileUpload.isMultipartContent(request)) return;

	    try {
	      factory = new DiskFileItemFactory();
	      uploadsManager = new ServletFileUpload(factory);
	      list = uploadsManager.parseRequest(request);
	      iterator = list.listIterator();

	      while (iterator.hasNext()) {
	        FileItem fileItem = iterator.next();
	        if (fileItem.isFormField()) {
	          try {
	            requestParameters.put(fileItem.getFieldName(), StreamHelper.toString(fileItem.getInputStream()).trim());
	          } catch(Exception e) {}
	        } else {
	          try {
	            requestParameters.put(fileItem.getFieldName(), fileItem.getInputStream());
	          } catch(Exception e) {}
	        }
	        
	      }
	    } catch (FileUploadException oException) {
	      return;
	    }  
	  }
	  
	  public static HashMap<String, Object> parseParameters(HttpServletRequest request) {
	    @SuppressWarnings("unchecked")
      Map<String, String[]> parameters = request.getParameterMap();
	    HashMap<String, Object> requestParameters = new HashMap<String, Object>();
	    
	    try {
	      
	      for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
	        String[] aValues = (String[])entry.getValue();
	        if (aValues.length <= 0) continue;
	        requestParameters.put(entry.getKey(), aValues[0].trim());
	      }
	      
	      LibraryRequest.parseMultipartParameters(request, requestParameters);
	    }
	    catch (Exception exception) {
	      exception.printStackTrace();
	    }
	    
	    return requestParameters;
	  }

}
