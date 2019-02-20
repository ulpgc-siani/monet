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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User extends BaseObject {
  private String    name;
  private UserInfo  info;
  private String    codeLanguage;
  private Date      registrationDate;
  
  public User() {
    super();
    this.name = "";
    this.info = new UserInfo();
    this.codeLanguage = new String("es");
    this.registrationDate = new Date();
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public UserInfo getInfo() {
    return this.info;
  }

  public void setInfo(UserInfo info) {
    this.info = info;
  }

  public String getLanguage() {
    return this.codeLanguage;
  }

  public void setLanguage(String code) {
    this.codeLanguage = code;
  }

  public Date getRegistrationDate() {
    return this.registrationDate;
  }

  public void setRegistrationDate(Date registration) {
    this.registrationDate = registration;
  }

  public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
    DateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss.S z");
    
    serializer.startTag("", "user");

    serializer.attribute("", "id", this.id);
    serializer.attribute("", "name", this.getName());
    serializer.attribute("", "codeLanguage", this.codeLanguage);
    serializer.attribute("", "registration", dateFormat.format(this.registrationDate));
    
    this.info.serializeToXML(serializer);
    
    serializer.endTag("", "user");
  }

  public void deserializeFromXML(Element node) throws ParseException {
    DateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss.S z");
    
    if (node.getAttribute("id") != null) this.id = node.getAttributeValue("id");
    if (node.getAttribute("name") != null) this.name = node.getAttributeValue("name");
    if (node.getAttribute("codeLanguage") != null) this.codeLanguage = node.getAttributeValue("codeLanguage");
    if (node.getAttribute("registration") != null) this.registrationDate = dateFormat.parse(node.getAttributeValue("registration"));
    
    if (node.getChild("info") != null) this.info.deserializeFromXML(node.getChild("info"));
  }
  
}
