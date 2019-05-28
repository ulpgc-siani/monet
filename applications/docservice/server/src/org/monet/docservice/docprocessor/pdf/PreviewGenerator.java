package org.monet.docservice.docprocessor.pdf;

public interface PreviewGenerator {

  void generatePreview(String pdfPath, String documentId, boolean checkCountPages);
  void generatePreview(String pdfPath, String documentId);

}
