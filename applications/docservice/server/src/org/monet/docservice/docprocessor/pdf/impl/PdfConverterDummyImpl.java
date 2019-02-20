package org.monet.docservice.docprocessor.pdf.impl;

import org.monet.docservice.docprocessor.pdf.PdfConverter;

public class PdfConverterDummyImpl implements PdfConverter {

  public void generatePdf(String sourceDocument, String destination) {
    throw new org.apache.commons.lang.NotImplementedException();
  }

  public boolean check() {
    return true;
  }
  
}
