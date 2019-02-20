package org.monet.docservice.docprocessor.pdf;

import java.io.InputStream;

public interface PdfXmlExtractor {

  String extractXmlData(InputStream sourcePdfFileStream);
  
}
