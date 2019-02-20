package org.monet.docservice.servlet.factory.impl;

import com.google.inject.Inject;
import org.monet.docservice.core.library.LibraryBase64;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.pdf.Signer;
import org.monet.docservice.servlet.RequestParams;
import org.monet.docservice.servlet.factory.MessageResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class StampDocumentSignature extends ActionStringResult {

  private Logger logger;
  private LibraryBase64 libraryBase64;
  private Signer signer;
  
  @Inject
  public void injectLogger(Logger logger){
    this.logger = logger;
  }
  
  @Inject
  public void injectLibraryBase64(LibraryBase64 libraryBase64) {
    this.libraryBase64 = libraryBase64;
  }
  
  @Inject
  public void injectSigner(Signer signer) {
    this.signer = signer;
  }
  
  @Override
  public String onExecute(Map<String, Object> params, HttpServletResponse response) throws Exception {
    String documentId = (String) params.get(RequestParams.REQUEST_PARAM_DOCUMENT_ID);
    String signId = (String) params.get(RequestParams.REQUEST_PARAM_SIGN_ID);
    String signature = (String) params.get(RequestParams.REQUEST_PARAM_SIGNATURE);
    
    logger.debug("stampDocumentSignature(%s,%s,%s)", documentId, signId, signature);

    byte[] pkcs7Block = this.libraryBase64.decode(signature);
    
    signer.signDocument(documentId, signId, pkcs7Block);
    
    return MessageResponse.OPERATION_SUCCESFULLY;
  }

}