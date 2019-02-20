package org.monet.docservice.docprocessor.templates;

public interface PartsExtractor {

  void processTemplate(String templatePath, String templateInstanceId, int type);

}