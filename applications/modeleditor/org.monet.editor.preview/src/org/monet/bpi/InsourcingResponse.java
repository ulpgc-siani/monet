package org.monet.bpi;

public interface InsourcingResponse {

  String getCode();
  String getContent();
  Node getNode(String name);
  
}
