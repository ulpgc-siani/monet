/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package org.monet.api.space.mailservice.impl.model;

import java.io.IOException;
import org.xmlpull.v1.XmlSerializer;


public class Email extends BaseObject {
  private String   from;
  private String   body;
  private Files files;
  
  public Email() {
    super();
    this.from = "";
    this.body = "";
  }

  public String getFrom() {
    return this.from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getBody() {
    return this.body;
  }

  public void setBody(String body) {
    this.body = body;
  }
  
  public Files getFiles() {
  	return this.files;
  }
  
  public void setFiles(Files files) {
  	this.files = files;
  }

  public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
    serializer.startTag("", "email");

    serializer.startTag("", "from");
    serializer.text(this.from);
    serializer.endTag("", "from");
    
    serializer.startTag("", "body");
    serializer.text(this.body);
    serializer.endTag("", "body");

    this.files.serializeToXML(serializer);
    
    serializer.endTag("", "email");
  }
  
}
