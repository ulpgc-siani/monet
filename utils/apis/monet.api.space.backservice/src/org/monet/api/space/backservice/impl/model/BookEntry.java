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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jdom.Element;
import org.xmlpull.v1.XmlSerializer;

public class BookEntry extends BaseObject {
  private String  idObject;
  private String  idUser;
  private String  author;
  private String  data;
  private Date    createDate;
  private Integer type;

  public BookEntry(String idObject, String sAuthor, Integer iType) {
    super();
    this.idUser  = sAuthor;
    this.author = sAuthor;
    this.idObject = idObject;
    this.data = "";
    this.type = iType;
    this.createDate = new Date();
  }
  
  public BookEntry() {
    this(null, null, null);
  }

  public String getIdUser() {
    return this.idUser;
  }

  public void setIdUser(String idUser) {
    this.idUser = idUser;
  }
  
  public String getAuthor() {
    return this.author;
  }

  public void setAuthor(String sAuthor) {
    this.author = sAuthor;
  }

  public String getIdObject() {
    return this.idObject;
  }

  public void setIdObject(String idNode) {
    this.idObject = idNode;
  }

  public String getData() {
    return this.data;
  }

  public void setData(String sData) {
    this.data = sData;
  }

  public Integer getType() {
    return this.type;
  }

  public void setType(Integer iType) {
    this.type = iType;
  }

  public Date getCreateDate() { 
    return this.createDate; 
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
    DateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss.S z");
    serializer.startTag("", "entry");
    serializer.attribute("", "idObject", this.idObject);
    serializer.attribute("", "type", String.valueOf(this.type));
    serializer.attribute("", "date", dateFormat.format(this.createDate));
    serializer.endTag("", "entry");
  }

  public void deserializeFromXML(Element node) throws ParseException {
	DateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss.S z");

	if (node.getAttribute("idObject") != null) this.idObject = node.getAttributeValue("idObject");
	if (node.getAttribute("type") != null) this.type = Integer.valueOf(node.getAttributeValue("type"));
	if (node.getAttribute("date") != null) this.createDate = dateFormat.parse(node.getAttributeValue("date"));
  }

}
