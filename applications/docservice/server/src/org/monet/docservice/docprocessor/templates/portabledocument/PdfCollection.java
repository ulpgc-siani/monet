package org.monet.docservice.docprocessor.templates.portabledocument;

public class PdfCollection {

  private String id;
  private int pageSize;
  private String offsetField;
  private boolean isMultipage;
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getId() {
    return id;
  }
  
  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }
  
  public int getPageSize() {
    return pageSize;
  }
  
  public boolean isMultipage() {
    return this.isMultipage;
  }
  
  public void setOffsetField(String offsetField) {
    this.offsetField = offsetField;
  }
  
  public String getOffsetField() {
    return offsetField;
  }

  public void setMultipage(boolean isMultipage) {
    this.isMultipage = isMultipage;
  }
  
}
