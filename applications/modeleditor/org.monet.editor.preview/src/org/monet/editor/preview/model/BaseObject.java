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

package org.monet.editor.preview.model;

import java.util.HashMap;

public abstract class BaseObject {
  protected String                   id;
  protected String                   code;
  protected String                   name;
  protected HashMap<String, Boolean> dirtyMap;

  public BaseObject() {
    this.id = "-1";
    this.code = "";
    this.name = "";
    this.dirtyMap = new HashMap<String, Boolean>();
  }

  public boolean isDirty(String code) {
    if (!this.dirtyMap.containsKey(code))
      return false;
    return this.dirtyMap.get(code);
  }

  public void setDirty(String code, Boolean value) {
    this.dirtyMap.put(code, value);
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCode() {
    return this.code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void clone(BaseObject baseModel) {
  }

}
