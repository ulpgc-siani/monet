package org.monet.bpi;

import org.monet.bpi.types.Link;

public interface InsourcingRequest {

  void setContent(String content);
  void addNode(String name, Node node);
  void addNode(String name, Link nodeLink);
  
}
