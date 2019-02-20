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

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class ModelDefinitionType extends BaseObject {
  private String label;
  
  public static final String NODE_CONTAINER  = "container";
  public static final String NODE_COLLECTION = "collection";
  public static final String NODE_DOCUMENT   = "document";
  public static final String NODE_REPORT     = "report";
  public static final String NODE_FORM       = "form";
  public static final String NODE_CATALOG    = "catalog";
  public static final String SERVICE         = "service";
  public static final String SERVICE_LINK    = "service-link";
  public static final String TASK            = "task";
  public static final String REFERENCE       = "reference";
  public static final String THESAURUS       = "thesaurus";


  public ModelDefinitionType() {
    super();
    this.label = new String("");
  }
  
  public void setCode(String code) {
    this.code = code;
  }

  public String getLabel() {
    return this.label;
  }

  public String getShortLabel(int length) {
    if (this.label.length() <= length) return this.label;
    return this.label.substring(0, length) + "...";
  }

  public void setLabel(String label) {
    this.label = label;
  }
  
  public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
    
  }
  
}
