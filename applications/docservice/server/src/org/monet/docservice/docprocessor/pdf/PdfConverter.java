package org.monet.docservice.docprocessor.pdf;


public interface PdfConverter {

  void generatePdf(String sourceDocument, String destination);

  boolean check();

}