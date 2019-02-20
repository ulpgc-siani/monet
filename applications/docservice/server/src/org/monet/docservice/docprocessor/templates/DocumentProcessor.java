package org.monet.docservice.docprocessor.templates;

import org.monet.docservice.docprocessor.templates.common.Model;

public interface DocumentProcessor {
  void setModel(Model model);
  void setDocumentId(String documentId);
  void process(String document);
}
