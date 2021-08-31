package org.monet.docservice.docprocessor.templates;

import org.monet.docservice.core.Key;
import org.monet.docservice.docprocessor.templates.common.Model;

public interface DocumentProcessor {
  void setModel(Model model);
  void setDocumentKey(Key documentKey);
  void process(String document);
}
