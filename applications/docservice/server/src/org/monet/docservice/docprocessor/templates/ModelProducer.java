package org.monet.docservice.docprocessor.templates;

import java.io.InputStream;

import org.monet.docservice.docprocessor.templates.common.Model;

public interface ModelProducer {

  Model create(InputStream dataStream);
  
}
