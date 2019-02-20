package org.monet.docservice.docprocessor.pdf.impl;

import java.io.InputStream;

import org.monet.docservice.core.log.Logger;
import org.monet.docservice.core.util.StreamHelper;
import org.monet.docservice.docprocessor.pdf.PdfXmlExtractor;

import com.google.inject.Inject;
import com.itextpdf.text.pdf.PdfReader;

public class PdfXmlExtractorImpl implements PdfXmlExtractor {

  private Logger logger;
  
  @Inject
  public void injectLogger(Logger logger) {
    this.logger = logger;
  }
  
  public String extractXmlData(InputStream sourcePdfFileStream) {
    this.logger.debug("extractXmlData(%s)", sourcePdfFileStream);
    
    PdfReader pdfReader = null;
    String xmlData = null;
    
    try {
      pdfReader = new PdfReader(sourcePdfFileStream);
      xmlData = pdfReader.getAcroFields().getField("xmlData");
    } catch(Exception e) {
      this.logger.error(e.getMessage(), e);
    } finally {
      if(pdfReader != null)
        pdfReader.close();
      StreamHelper.close(sourcePdfFileStream);
    }

    return xmlData;
  }

}
