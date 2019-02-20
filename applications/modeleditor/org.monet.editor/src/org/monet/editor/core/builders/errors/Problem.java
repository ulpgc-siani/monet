package org.monet.editor.core.builders.errors;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

public abstract class Problem {

  public final static String MARKER_ID = "org.isi.monet.modeling.compiler.compilerproblems"; 
  
	private String message;
	private IResource resource;
	private int severity;
	private int numberLine;

  public Problem(IResource resource, String message,int numberLine, int severity) {
    this.message = message;
    this.resource = resource;
    this.severity = severity;
    this.setNumberLine(numberLine == -1 ? 1 : numberLine);
  }
	
  protected void setMessage(String message) {
	this.message = message;
  }
  
  public String getMessage() {
	return message;
  }
  
  public void setResource(IResource resource) {
    this.resource = resource;
  }
  
  public IResource getResource() {
    return resource;
  }
  
  public void setSeverity(int severity) {
    this.severity = severity;
  }
  
  public int getSeverity() {
    return severity;
  }
  
  public void setNumberLine(int numberLine) {
	this.numberLine = numberLine;
  }

  public int getNumberLine() {
	return numberLine;
  }
	
  public void show() throws CoreException {
    if(resource!= null && resource.exists()) {
      IMarker marker = resource.createMarker(IMarker.PROBLEM);
      marker.setAttribute(IMarker.MESSAGE, message);
      marker.setAttribute(IMarker.SEVERITY, severity);
      marker.setAttribute(IMarker.LINE_NUMBER, numberLine);
    }
  }

}
