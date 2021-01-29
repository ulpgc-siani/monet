package org.monet.docservice.docprocessor.templates;

import org.monet.docservice.core.Key;

public interface PartsExtractor {

  void processTemplate(String templatePath, String templateInstanceId, int type);

}