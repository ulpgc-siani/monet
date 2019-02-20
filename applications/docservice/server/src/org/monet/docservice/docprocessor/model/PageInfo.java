package org.monet.docservice.docprocessor.model;

public class PageInfo {
  private int id;
  private int width;
  private int height;
  private float aspectRatio;
  
  public void setId(int id) {
    this.id = id;
  }
  public int getId() {
    return id;
  }
  public void setWidth(int width) {
    this.width = width;
  }
  public int getWidth() {
    return width;
  }
  public void setHeight(int height) {
    this.height = height;
  }
  public int getHeight() {
    return height;
  }
  public void setAspectRatio(float aspectRatio) {
    this.aspectRatio = aspectRatio;
  }
  public float getAspectRatio() {
    return aspectRatio;
  }
}
