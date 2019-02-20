package org.monet.grided.core.model;

import java.io.StringWriter;

import org.simpleframework.xml.core.Persister;

public class ImportError {  
  
  private static final String NL = System.getProperty("line.separator");
  private final String label;
  private final Throwable throwable;
  private final Object item;
  
  public ImportError(String label, Throwable throwable, Object item) {
    this.label = label;   
    this.throwable = throwable;
    this.item = item;
  }
  
  public String getLabel() {
    return this.label;
  }
  
  public String getStackTrace() {
    StringBuilder sb = new StringBuilder();   
    
    for (Throwable e = throwable; e != null; e = e.getCause()) {
      sb.append(NL);
      sb.append(e == throwable ? "Exception: " : "Caused By: ").append(NL);          

      sb.append(e.getClass().getName()).append(NL);
      sb.append("Message: ").append(e.getMessage()).append(NL).append(NL);     
      StackTraceElement[] elems = e.getStackTrace();
      if (elems != null && elems.length > 0) {
        sb.append("Stack trace: ").append(NL);

        for (StackTraceElement elem : elems) {
          sb.append("   at ");
          sb.append(elem.getClassName());
          sb.append('.');
          sb.append(elem.getMethodName());
          sb.append('(');
          sb.append(elem.getFileName());
          sb.append(':');
          sb.append(Math.max(1, elem.getLineNumber()));
          sb.append(')');
          sb.append(NL);
        }
      }
    }
    return sb.toString();    
  }
  
  public String getItem() throws Exception {
    Persister persister = new Persister();
    StringWriter writer = new StringWriter();
    persister.write(this.item, writer);
    return writer.toString();
  }
}
