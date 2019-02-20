package org.monet.docservice.docprocessor.templates;

import java.io.InputStream;

public interface DocumentReplacer {

  public void updateDocument(String documentId, String sDocumentPath, InputStream dataStream, int type);
  
}
