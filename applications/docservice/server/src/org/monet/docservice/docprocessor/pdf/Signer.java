package org.monet.docservice.docprocessor.pdf;

import org.monet.docservice.core.Key;
import org.monet.docservice.docprocessor.model.PresignedDocument;

public interface Signer {

  PresignedDocument prepareDocument(Key documentKey, byte[] aCertificate, String reason, String location, String contact, String signField);
  void signDocument(Key documentKey, String instanceId, byte[] pkcs7Block);
  
}
