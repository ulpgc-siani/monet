package org.monet.mobile.service.federation;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="response")
public class Response {

  private static final String CODE_IP_SUSPEND = "IP_SUSPEND"; 
  private static final String CODE_LOGIN_SUCCESSFULLY = "SUCCESSFULLY";
//  private static final String CODE_LOGIN_INCORRECT = "INCORRECT";
  
  public static final int RESPONSE_IP_SUSPEND = 3; 
  public static final int RESPONSE_LOGIN_SUCCESSFULLY = 1;
  public static final int RESPONSE_LOGIN_INCORRECT = 2;
  
  @Attribute(name="code")
  private String code; 
  
  @Element(name="verifier")
  private String verifier;

  public void setCode(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }
  
  public int getResponseCode() {
    if(code.equals(CODE_LOGIN_SUCCESSFULLY))
      return RESPONSE_LOGIN_SUCCESSFULLY;
    else if(code.equals(CODE_IP_SUSPEND))
      return RESPONSE_IP_SUSPEND;
    else // if(code.equals(CODE_LOGIN_INCORRECT))
      return RESPONSE_LOGIN_INCORRECT;
  }

  public void setVerifier(String verifier) {
    this.verifier = verifier;
  }

  public String getVerifier() {
    return verifier;
  }
  
}
