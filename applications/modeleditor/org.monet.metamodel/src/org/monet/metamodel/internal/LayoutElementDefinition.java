package org.monet.metamodel.internal;
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

import javax.xml.bind.annotation.XmlAttribute;

public abstract class LayoutElementDefinition {
  private @XmlAttribute(name="width", required=false) String width;
  private @XmlAttribute(name="height", required=false) String height;
  private @XmlAttribute(name="edition", required=false) String edition;
  private @XmlAttribute(name="placeholder", required=false) String placeHolder;
  private @XmlAttribute(name="hint", required=false) String hint;

  public LayoutElementDefinition() {
    super();
  }
  
  public String getWidth() {
    return width;
  }

  public String getHeight() {
    return height;
  }

  public String getEdition() {
    return edition;
  }

  public String getPlaceHolder() {
    return placeHolder;
  }

  public String getHint() {
    return hint;
  }

}
