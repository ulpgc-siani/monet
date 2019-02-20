package org.monet.grided.library;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

public class LibraryMultipartRequest {

  private DiskFileItemFactory factory;
  private ServletFileUpload uploadManager;
  private HttpServletRequest request;
  List<FileItem> parametersList = new LinkedList<FileItem>();

  public LibraryMultipartRequest(HttpServletRequest request) {
    this.request = request;
    this.factory = new DiskFileItemFactory();
    this.uploadManager = new ServletFileUpload(factory);   
    
     this.parametersList = this.parseRequest();
  }
    
  public String getParameter(String parameterName) {   
    Iterator<FileItem> iter = this.parametersList.iterator();
             
    while (iter.hasNext()) {
      FileItem item = iter.next();
      if (parameterName.equals(item.getFieldName())) {
        return item.getString();
      }
    }      
    return "";
  }
  
  public FileItem getFileParameter(String parameterName) {
    Iterator<FileItem> iter = this.parametersList.iterator();
    
    while (iter.hasNext()) {
      FileItem item = iter.next();
      if (item.isFormField()) continue;
      if (parameterName.equals(item.getFieldName())) {
        return item;
      }
    }      
    return null;    
  }

  private List<FileItem> parseRequest() {
    List<FileItem> list = new ArrayList<FileItem>();
    try {
      list = uploadManager.parseRequest(this.request);
    } catch (FileUploadException e) {
      // TODO throw exception
      e.printStackTrace();
    }
    return list;
  }
}
