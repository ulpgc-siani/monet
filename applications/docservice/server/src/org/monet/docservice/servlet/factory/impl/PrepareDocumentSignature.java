package org.monet.docservice.servlet.factory.impl;

import com.google.inject.Inject;
import org.monet.docservice.core.Key;
import org.monet.docservice.core.library.LibraryBase64;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.model.PresignedDocument;
import org.monet.docservice.docprocessor.pdf.Signer;
import org.monet.docservice.servlet.RequestParams;
import org.simpleframework.xml.Serializer;

import javax.servlet.http.HttpServletResponse;
import java.io.StringWriter;
import java.util.Map;

public class PrepareDocumentSignature extends ActionStringResult {

  private Logger logger;
  private LibraryBase64 libraryBase64;
  private Signer signer;
  private Serializer serializer;
  
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
  
  @Inject
  public void injectSerializer(Serializer serializer) {
    this.serializer = serializer;
  }
  
  @Override
  public String onExecute(Map<String, Object> params, HttpServletResponse response) throws Exception {
    Key documentKey = documentKeyFromId(params);
    String certificate = (String) params.get(RequestParams.REQUEST_PARAM_CERTIFICATE);
    String reason = (String)  params.get(RequestParams.REQUEST_PARAM_SIGN_REASON);
    String location = (String)  params.get(RequestParams.REQUEST_PARAM_SIGN_LOCATION);
    String contact = (String)  params.get(RequestParams.REQUEST_PARAM_SIGN_CONTACT);
    String signField = (String)  params.get(RequestParams.REQUEST_PARAM_SIGN_FIELD);
    
    logger.debug("prepareDocumentSignature(%s,%s)", documentKey, certificate);

    byte[] binaryCert = this.libraryBase64.decode(certificate);
    PresignedDocument result = signer.prepareDocument(documentKey, binaryCert, reason, location, contact, signField);
    
    StringWriter writer = new StringWriter();
    this.serializer.write(result, writer);
    return writer.toString();
  }

}