package org.monet.docservice.docprocessor.model;

public class DocumentType {

  public static final int OPEN_DOCUMENT = 1;
  public static final int OPEN_XML = 2;
  public static final int PORTABLE_DOCUMENT = 3;
  public static final int XML_DOCUMENT = 4;
  
  public static int valueOf(String contentType) {
    if(contentType.equals("application/vnd.oasis.opendocument.text")) {
      return OPEN_DOCUMENT;
    } else if(contentType.equals("application/pdf")) {
      return PORTABLE_DOCUMENT;
    } else if(contentType.equals("text/xml") || contentType.equals("application/xml")) {
      return XML_DOCUMENT;
    } else {
      return OPEN_XML;
    }
  }
  
  public static String toString(int type) {
    switch(type) {
      case OPEN_DOCUMENT:
        return "application/vnd.oasis.opendocument.text";
      case OPEN_XML:
        return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
      case PORTABLE_DOCUMENT:
        return "application/pdf";
      case XML_DOCUMENT:
        return "text/xml";
    }
    return null;
  }
  
}
