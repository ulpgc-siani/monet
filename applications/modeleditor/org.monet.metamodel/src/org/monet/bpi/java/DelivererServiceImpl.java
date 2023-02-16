package org.monet.bpi.java;

import java.net.URI;
import java.util.Map;

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
  public void deliver(URI url, NodeDocument document, Map<String, String> headers) throws Exception {
  }

  @Override
  public void deliver(URI url, Map<String, String> params) throws Exception {
  }

  @Override
  public void deliver(URI url, Map<String, String> params, Map<String, String> headers) throws Exception {
  }

  @Override
  public void deliverJson(URI url, Map<String, Object> params) throws Exception {
  }

  @Override
  public void deliverJson(URI url, Map<String, Object> params, Map<String, String> headers) throws Exception {
  }

  @Override
  public void deliverJson(URI url, String body) throws Exception {
  }

  @Override
  public void deliverJson(URI url, String body, Map<String, String> headers) throws Exception {
  }

  @Override
  public void deliverToMail(URI from, URI to, String subject, String body, NodeDocument document) {
  }

}
