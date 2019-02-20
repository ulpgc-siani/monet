package org.monet.federation.setupservice.core.model;

import java.io.StringWriter;
import java.util.Date;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

@Root(name="status")
public class Status {

  private static Serializer persister = new Persister();
  @Attribute(name="runningDate") long runningDate;
  public void setRunningDate(Date runningDate) {
    if(runningDate == null) this.runningDate = -1;
    this.runningDate = runningDate.getTime();   
  }
  
  public String serializeToXML() throws Exception {
    StringWriter writer = new StringWriter();
    persister.write(this, writer);
    return writer.toString();
  }
}
