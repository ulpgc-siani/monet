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

import java.io.IOException;
import java.text.ParseException;

import org.jdom.Element;
import org.xmlpull.v1.XmlSerializer;


public class UserInfo extends BaseObject {
  private String   photo;
  private String   fullname;
  private String   preferences;
  private String   email;
  private String[] occupations;
  
  public UserInfo() {
    super();
    this.photo = "";
    this.fullname = "";
    this.preferences = "";
    this.occupations = new String[0];
    this.email = "";
  }

  public String getPhoto() {
    return this.photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }

  public String getFullname() {
    return this.fullname;
  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

  public String getEmail() {
    return this.email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }

  public String getPreferences() {
    return this.preferences;
  }

  public void setPreferences(String preferences) {
    this.preferences = preferences;
  }

  public String[] getOccupations() {
    return this.occupations;
  }
  
  public void setOccupations(String[] occupations) {
    this.occupations = occupations;
  }

  public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
    serializer.startTag("", "info");

    serializer.startTag("", "fullname");
    serializer.text(this.fullname);
    serializer.endTag("", "fullname");
    
    serializer.startTag("", "preferences");
    serializer.text(this.preferences);
    serializer.endTag("", "preferences");

    serializer.startTag("", "email");
    serializer.text(this.email);
    serializer.endTag("", "email");

    serializer.endTag("", "info");
  }
  
  public void deserializeFromXML(Element userInfo) throws ParseException {
    if (userInfo.getChild("fullname") != null) this.fullname = userInfo.getChild("fullname").getText();
    if (userInfo.getChild("preferences") != null) this.preferences = userInfo.getChild("preferences").getText();
    if (userInfo.getChild("email") != null) this.email = userInfo.getChild("email").getText();
  }

}
