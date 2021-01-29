package org.monet.docservice.docprocessor.pdf;

import org.monet.docservice.core.Key;

public interface PreviewGenerator {

  void generatePreview(String pdfPath, Key documentKey);

}
