package org.monet.editor.preview.model;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;


public class Problem {

  public static final String SEVERITY_WARNING = "Warning";
  public static final String SEVERITY_ERROR = "Error";
  
  private String errorType;
	private String message;
	private String internalMessage;
	private String severity;
	private String stage;

	public Problem() {}
	
  public Problem(String message, String errorType, String internalMessage, String severity) {
    this.errorType = errorType;
    this.message = message;
    this.internalMessage = internalMessage;
    this.severity = severity;
  }
	
  protected void setMessage(String message) {
    this.message = message;
  }
  
  public String getMessage() {
    return message;
  }
  
  public void setSeverity(String severity) {
    this.severity = severity;
  }

  public String getSeverity() {
    return severity;
  }
  
  public void setStage(String stage) {
    this.stage = stage;
  }

  public String getStage() {
    return stage;
  }

  public JSONObject toJson() {
    JSONObject jsonObject = new JSONObject();
    
    jsonObject.put("stage", stage);
    jsonObject.put("message", JSONUtils.stripQuotes(message));
    jsonObject.put("internalMessage", internalMessage);
    jsonObject.put("severity", severity);
    jsonObject.put("errorType", errorType);
    
    return jsonObject;
  }

}
