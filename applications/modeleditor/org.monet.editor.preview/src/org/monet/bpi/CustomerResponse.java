package org.monet.bpi;

import java.io.File;

public interface CustomerResponse {

  void setContent(String content);
  void attachFile(String name, File file);
  void attachDocument(String name, NodeDocument document);
  void attachSchema(String name, Schema schema);
  void attachSchema(String name, NodeDocument document);
  void attachString(String name, String content);
  
}
