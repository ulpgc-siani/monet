package org.monet.docservice.docprocessor.templates;

import org.monet.docservice.core.Key;

import java.io.InputStream;

public interface DocumentReplacer {

  void updateDocument(Key documentKey, String sDocumentPath, InputStream dataStream, int type);
  
}
