package org.monet.federation.accountservice.accountactions.impl.messagemodel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "SocketResponseMessage")
public class SocketResponseMessage implements Serializable {
  private static final long serialVersionUID = 5293261136158384562L;
  
  public static final String RESPONSE_OK                     = "OK";
  public static final String RESPONSE_FALSE                  = "FALSE";

  public static final String RESPONSE_ERROR_UNKNOW           = "ERROR_UNKNOW";
  public static final String RESPONSE_USER_NOT_FOUND         = "USER_NOT_FOUND";
  public static final String RESPONSE_CLOSE_CONNECTION       = "CLOSE_CONNECTION";
  public static final String RESPONSE_FEDERATION_IS_STOP     = "FEDERATION_IS_STOP";

  @Attribute
  private String id;
  @Attribute
  private boolean            isError;
  @Element
  private String response;

  public String getId() {
    return id;
  }

  public String getResponse() {
    return response;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setResponse(String response) {
    this.response = response;
  }

  public boolean isError() {
    return isError;
  }

  public void setIsError(boolean isError) {
    this.isError = isError;
  }
}
