package org.monet.docservice.core.log;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;


public class EventLog {

  private String logger;
  private String priority;
  private String message;
  private String stacktrace;
  private Date creationTime;
  
  public void setLogger(String logger) {
    this.logger = logger;
  }
  public String getLogger() {
    return logger;
  }
  public void setPriority(String priority) {
    this.priority = priority;
  }
  public String getPriority() {
    return priority;
  }
  public void setMessage(String message) {
    this.message = message;
  }
  public String getMessage() {
    return message;
  }
  public void setStacktrace(String stacktrace) {
    this.stacktrace = stacktrace;
  }
  public String getStacktrace() {
    return stacktrace;
  }
  public void setCreationTime(Date creationTime) {
    this.creationTime = creationTime;
  }
  public Date getCreationTime() {
    return creationTime;
  }
  
  public String toJSON() {
    JSONObject result = new JSONObject();
    result.put("logger", this.logger);
    result.put("priority", this.priority);
    result.put("message", this.message);
    result.put("stacktrace", this.stacktrace);
    result.put("creationtime", (new SimpleDateFormat("yyyy/MM/dd hh:mm:ss")).format(this.creationTime));
    return result.toString();
  }
  
}
