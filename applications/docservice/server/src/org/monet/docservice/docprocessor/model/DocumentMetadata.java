package org.monet.docservice.docprocessor.model;

import java.util.HashMap;
import java.util.Map;

public class DocumentMetadata {
  private String documentId;
  private boolean isDeprecated;
  private Map<Integer, PageInfo> pagesInfo = new HashMap<Integer, PageInfo>();
  private int numberOfPages;
  private boolean hasPendingOperations;
  private long estimatedTimeToFinish;
  
  public void setDocumentId(String documentId) {
    this.documentId = documentId;
  }
  public String getDocumentId() {
    return documentId;
  }
 
  public Map<Integer, PageInfo> getPages() {
    return pagesInfo;
  }
  
  public int getNumberOfPages() {
    return this.numberOfPages;
  }
  
  public void setPages(Map<Integer, PageInfo> pagesInfo) {
    this.pagesInfo = pagesInfo;
    this.numberOfPages = pagesInfo.size();
  }
  
  public void addPage(int id, int width, int height, float aspectRatio) {
    PageInfo pageInfo = new PageInfo();
    pageInfo.setId(id);
    pageInfo.setWidth(width);
    pageInfo.setHeight(height);
    pageInfo.setAspectRatio(aspectRatio);
    pagesInfo.put(id, pageInfo);
    
    this.numberOfPages++;
  }
  public void setDeprecated(boolean deprecated) {
    this.isDeprecated = deprecated;    
  }
  
  public boolean isDeprecated() {
    return this.isDeprecated;
  }
  public void setHasPendingOperations(boolean documentHasPendingOperations) {
    this.hasPendingOperations = documentHasPendingOperations;
  }
  public boolean getHasPendingOperations() {
    return this.hasPendingOperations;
  }
  public void setEstimatedTimeToFinish(long estimatedTimeToFinish) {
    this.estimatedTimeToFinish = estimatedTimeToFinish;
  }
  public long getEstimatedTimeToFinish() {
    return this.estimatedTimeToFinish;
  }
}