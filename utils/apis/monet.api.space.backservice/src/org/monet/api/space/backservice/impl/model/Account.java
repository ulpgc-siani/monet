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

package org.monet.api.space.backservice.impl.model;

import org.jdom.Element;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.ParseException;

public class Account extends BaseObject {
  private String fullname;
  private String email;

  public Account() {
    this.id = "";
    this.code = "";
    this.fullname = "";
    this.email = "";
  }

  public String getFullname() {
    return fullname;
  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
    serializer.startTag("", "account");

    serializer.attribute("", "id", this.id);
    serializer.attribute("", "code", this.getCode());
    serializer.attribute("", "fullname", this.getFullname());
    serializer.attribute("", "email", this.getEmail());
    
    serializer.endTag("", "account");
  }

  public void deserializeFromXML(Element node) throws ParseException {
    if (node.getAttribute("id") != null) this.id = node.getAttributeValue("id");
    if (node.getAttribute("code") != null) this.code = node.getAttributeValue("code");
    if (node.getAttribute("fullname") != null) this.fullname = node.getAttributeValue("fullname");
    if (node.getAttribute("email") != null) this.email = node.getAttributeValue("email");
  }

}
