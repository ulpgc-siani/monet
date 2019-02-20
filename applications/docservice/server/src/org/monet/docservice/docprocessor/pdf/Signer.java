package org.monet.docservice.docprocessor.pdf;

import org.monet.docservice.docprocessor.model.PresignedDocument;

public interface Signer {

  PresignedDocument prepareDocument(String documentId, byte[] aCertificate, String reason, String location, String contact, String signField);
  void signDocument(String documentId, String instanceId, byte[] pkcs7Block);
  
}
