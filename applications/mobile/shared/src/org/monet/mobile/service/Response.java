package org.monet.mobile.service;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="response")
public class Response {

  @Element(required=false)
  private Result result;
  
  @Element(required=false)
  private ErrorResult error;

  public Response() {}
  
  public Response(ErrorResult error) {
    this.error = error;
  }
  
  public Response(Result result) {
    this.result = result;
  }
  
  public void setData(Result result) {
    this.result = result;
  }

  public Result getResult() {
    return result;
  }

  public void setError(ErrorResult error) {
    this.error = error;
  }

  public ErrorResult getError() {
    return error;
  }

  public boolean isError() {
    return this.error != null;
  }
  
  
}
