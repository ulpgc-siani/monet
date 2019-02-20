package org.monet.docservice.tests.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.junit.Test;
import org.monet.docservice.docprocessor.model.DocumentType;
import org.monet.filesystem.StreamHelper;
import org.monet.http.RestfullClient;


public class DocServiceREST {

  public static final String DOCSERVICE_BASE_URL = "http://localhost:8080/monet.docservice/document/";
  public static final String REQUEST_PARAM_ACTION               = "action";
  public static final String REQUEST_PARAM_TEMPLATE_CODE        = "templateCode";
  public static final String REQUEST_PARAM_TEMPLATE_DATA        = "templateData";
  public static final String REQUEST_PARAM_MIME_TYPE            = "mimeType";
  public static final String REQUEST_PARAM_SIGN_DATA            = "signData";
  public static final String REQUEST_PARAM_DOCUMENT_CODE        = "documentCode";
  public static final String REQUEST_PARAM_DOCUMENT_DATA        = "documentData";
  public static final String REQUEST_PARAM_CONTENT_TYPE         = "contentType";
  public static final String REQUEST_PARAM_GENERATE_PREVIEW     = "generatePreview";
  public static final String REQUEST_PARAM_WIDTH                = "width";
  public static final String REQUEST_PARAM_HEIGHT               = "height";
  public static final String REQUEST_PARAM_ASYNCRONOUS          = "asynchronous";
  public static final String REQUEST_PARAM_NODE_CODE            = "nodeCode";

  @Test
  public void uploadTemplate() throws Exception {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    String code = "ptap";
    String templatePath = "resources/oxf/Template.docx";
    String mimeType = DocumentType.toString(DocumentType.OPEN_XML);
    InputStream template = this.getFileAsByteArray(templatePath);
    parameters.put(REQUEST_PARAM_ACTION , new StringBody("uploadTemplate"));
    parameters.put(REQUEST_PARAM_TEMPLATE_CODE , new StringBody(code));
    parameters.put(REQUEST_PARAM_MIME_TYPE,  new StringBody(mimeType));
    parameters.put(REQUEST_PARAM_TEMPLATE_DATA, new InputStreamBody(template,code));
    RestfullClient.execute(DOCSERVICE_BASE_URL, parameters);
  }

  @Test
  public void uploadTemplateWithSigns() throws Exception {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    String code = "ptapSign";
    String templatePath = "resources/oxf/Template.docx";
    String signPath = "resources/xml/signatures.xml";
    String mimeType = DocumentType.toString(DocumentType.OPEN_XML);
    InputStream template = this.getFileAsByteArray(templatePath);
    InputStream signs = this.getFileAsByteArray(signPath);
    parameters.put(REQUEST_PARAM_ACTION , new StringBody("uploadTemplateWithSigns"));
    parameters.put(REQUEST_PARAM_TEMPLATE_CODE , new StringBody(code));
    parameters.put(REQUEST_PARAM_MIME_TYPE,  new StringBody(mimeType));
    parameters.put(REQUEST_PARAM_TEMPLATE_DATA, new InputStreamBody(template,code));
    parameters.put(REQUEST_PARAM_SIGN_DATA, new InputStreamBody(signs,code));
    RestfullClient.execute(DOCSERVICE_BASE_URL, parameters);
  }

  @Test
  public void uploadDocument() throws Exception {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    String code = "fecytDocument";
    String contentType = "application/pdf";
    String documentPath = "resources/pdf/fecyt_PN2008.pdf";
    InputStream document = this.getFileAsByteArray(documentPath);
    parameters.put(REQUEST_PARAM_ACTION , new StringBody("uploadDocument"));
    parameters.put(REQUEST_PARAM_DOCUMENT_CODE , new StringBody(code));
    parameters.put(REQUEST_PARAM_CONTENT_TYPE,  new StringBody(contentType));
    parameters.put(REQUEST_PARAM_DOCUMENT_DATA, new InputStreamBody(document,code));
    parameters.put(REQUEST_PARAM_GENERATE_PREVIEW, new StringBody("true"));
    RestfullClient.execute(DOCSERVICE_BASE_URL, parameters);
  }

  @Test
  public void uploadImage() throws Exception {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    String code = "image";
    String contentType = "image/jpeg";
    String imagePath = "resources/images/image.jpg";
    InputStream image = this.getFileAsByteArray(imagePath);
    int width  = 1680;
    int height = 1050;
    parameters.put(REQUEST_PARAM_ACTION , new StringBody("uploadImage"));
    parameters.put(REQUEST_PARAM_DOCUMENT_CODE , new StringBody(code));
    parameters.put(REQUEST_PARAM_CONTENT_TYPE,  new StringBody(contentType));
    parameters.put(REQUEST_PARAM_DOCUMENT_DATA, new InputStreamBody(image,code));
    parameters.put(REQUEST_PARAM_WIDTH, new StringBody(String.valueOf(width)));
    parameters.put(REQUEST_PARAM_HEIGHT, new StringBody(String.valueOf(height)));
    RestfullClient.execute(DOCSERVICE_BASE_URL, parameters);
  }

  @Test
  public void createDocument() throws Exception {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    String templateCode = "ptap";
    String documentCode = "documentPtap3";
    parameters.put(REQUEST_PARAM_ACTION , new StringBody("createDocument"));
    parameters.put(REQUEST_PARAM_TEMPLATE_CODE , new StringBody(templateCode));
    parameters.put(REQUEST_PARAM_DOCUMENT_CODE,  new StringBody(documentCode));
    RestfullClient.execute(DOCSERVICE_BASE_URL, parameters);
  }

  @Test
  public void updateDocument() throws Exception {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    String documentCode = "documentPtap1";
    String documentPath = "resources/docModels/dataPTAP.xml";
    InputStream document = this.getFileAsByteArray(documentPath);
    parameters.put(REQUEST_PARAM_ACTION , new StringBody("updateDocument"));
    parameters.put(REQUEST_PARAM_DOCUMENT_CODE,  new StringBody(documentCode));
    parameters.put(REQUEST_PARAM_DOCUMENT_DATA, new InputStreamBody(document,documentCode));
    parameters.put(REQUEST_PARAM_ASYNCRONOUS,  new StringBody("false"));
    RestfullClient.execute(DOCSERVICE_BASE_URL, parameters);
  }

  @Test
  public void existsDocument() throws Exception {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    String documentCode = "documentPtap";
    parameters.put(REQUEST_PARAM_ACTION , new StringBody("existsDocument"));
    parameters.put(REQUEST_PARAM_DOCUMENT_CODE,  new StringBody(documentCode));
    InputStream result = (InputStream)RestfullClient.execute(DOCSERVICE_BASE_URL, parameters);
    System.out.println(StreamHelper.toString(result));
  }

  @Test
  public void consolidateDocument() throws Exception {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    String documentCode = "documentPtap1";
    parameters.put(REQUEST_PARAM_ACTION , new StringBody("consolidateDocument"));
    parameters.put(REQUEST_PARAM_DOCUMENT_CODE,  new StringBody(documentCode));
    parameters.put(REQUEST_PARAM_ASYNCRONOUS,  new StringBody("true"));
    RestfullClient.execute(DOCSERVICE_BASE_URL, parameters);
  }

  @Test
  public void removeAllNodeFiles() throws Exception {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    String nodeCode = "111";
    parameters.put(REQUEST_PARAM_ACTION , new StringBody("removeAllNodeFiles"));
    parameters.put(REQUEST_PARAM_DOCUMENT_CODE,  new StringBody(nodeCode));
    InputStream result = (InputStream)RestfullClient.execute(DOCSERVICE_BASE_URL, parameters);
    System.out.println(StreamHelper.toString(result));
  }

  @Test
  public void getDocument() throws Exception {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    String documentCode = "documentPtap";
    parameters.put(REQUEST_PARAM_ACTION , new StringBody("getDocument"));
    parameters.put(REQUEST_PARAM_DOCUMENT_CODE,  new StringBody(documentCode));
    InputStream file = (InputStream)RestfullClient.execute(DOCSERVICE_BASE_URL, parameters);
    FileOutputStream out = new FileOutputStream("C:\\Users\\fsantana\\Desktop\\tmp\\getDocument.pdf");
    StreamHelper.copy(file, out);
  }

  @Test
  public void getDocumentXmlData() throws Exception {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    String documentCode = "documentPtap";
    parameters.put(REQUEST_PARAM_ACTION , new StringBody("getDocumentXmlData"));
    parameters.put(REQUEST_PARAM_DOCUMENT_CODE,  new StringBody(documentCode));
    InputStream result = (InputStream)RestfullClient.execute(DOCSERVICE_BASE_URL, parameters);
    System.out.println(StreamHelper.toString(result));
  }

  @Test
  public void getDocumentHash() throws Exception {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    String documentCode = "documentPtap";
    parameters.put(REQUEST_PARAM_ACTION , new StringBody("getDocumentHash"));
    parameters.put(REQUEST_PARAM_DOCUMENT_CODE,  new StringBody(documentCode));
    InputStream result = (InputStream)RestfullClient.execute(DOCSERVICE_BASE_URL, parameters);
    System.out.println(StreamHelper.toString(result));
  }

  public InputStream getFileAsByteArray(String path) throws Exception{
    File template = new File(path);
    FileInputStream inputStream = null;
    inputStream = new FileInputStream(template);
    return inputStream;
  }
}
