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

package org.monet.v2.model;

import net.sf.json.JSONObject;
import org.monet.v2.model.constants.Strings;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

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

  public JSONObject serializeToJSON() {
  	JSONObject result = new JSONObject();

    result.put("photo", this.getPhoto());
    result.put("fullname", this.getFullname());
    result.put("preferences", this.getPreferences());
    result.put("email", this.getEmail());
    result.put("occupations", LibraryArray.implode(this.getOccupations(), Strings.COMMA));
    
    return result;
  }

  public void unserializeFromJSON(String data) {
    JSONObject jsonData = JSONObject.fromObject(data);
    String occupations = jsonData.getString("occupations"); 

    this.fullname = jsonData.getString("fullname"); 
    this.preferences = jsonData.getString("preferences");
    this.email = jsonData.getString("email");
    this.occupations = ((occupations != null) && (!occupations.equals("")))?occupations.split(Strings.COMMA):new String[0];

  }
  
  public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
    
  }

}
