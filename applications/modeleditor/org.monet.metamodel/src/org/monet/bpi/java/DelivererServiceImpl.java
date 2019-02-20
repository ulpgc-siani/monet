package org.monet.bpi.java;

import java.net.URI;

import org.monet.bpi.DelivererService;
import org.monet.bpi.NodeDocument;


public class DelivererServiceImpl extends DelivererService {

  public static void init() {
    instance = new DelivererServiceImpl();
  }

  @Override
  public void deliver(URI url, NodeDocument document) throws Exception {
  }

  @Override
  public void deliverToMail(URI from, URI to, String subject, String body, NodeDocument document) {
  }

}
