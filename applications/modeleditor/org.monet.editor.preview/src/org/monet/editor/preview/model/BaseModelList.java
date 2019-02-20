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

import java.util.Iterator;

public class BaseModelList<T extends BaseObject> extends BaseObject implements Iterable<T> {
  protected MonetHashMap<T> items;
  protected MonetHashMap<String> codes;
  protected String       content;
  protected int          totalCount;
  
  public BaseModelList() {
  	super();
    this.items = new MonetHashMap<T>();
    this.codes = new MonetHashMap<String>();
    this.content = "";
    this.totalCount = -1;
  }
  
  private String generateId() {
    int id = this.items.size();
    while (this.items.containsKey(String.valueOf(id))) id++;
    return String.valueOf(id);
  }

  public String getContent() {
    return this.content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public boolean exist(String idCode) {
    if (this.items.containsKey(idCode)) return true;
    if (this.codes.containsKey(idCode)) return true;
    return false;
  }

  @Override
  public Iterator<T> iterator() {
    return this.get().values().iterator();
  }

  public MonetHashMap<T> get() {
    return this.items;
  }

  public T get(String codeId) {
    MonetHashMap<T> items = this.get();
    if (items.containsKey(codeId)) return items.get(codeId);
    if (! this.codes.containsKey(codeId)) return null;
    return items.get(this.codes.get(codeId));
  }

  public void add(T baseModel) {
    String id, code;
    
    id = baseModel.getId();
    if ((id.equals("-1")) || (id.isEmpty())) id = baseModel.getName();
    if (id.equals("-1")) id = this.generateId();

    baseModel.setId(id);
    this.items.put(id, baseModel);

    code = baseModel.getCode();
    if ((code != null) && (! code.equals("-1"))) this.codes.put(code, id);
    this.totalCount += 1;
  }

  public void delete(String codeId) {
    String id = null;
    
    if (this.items.containsKey(codeId)) id = codeId;
    if ((id == null) && (this.codes.containsKey(codeId))) id = (String)this.codes.get(codeId);
    if (id == null) return;
    
    this.items.remove(id);
    this.codes.remove(id);
  }

  public void clear() {
    this.items.clear();
    this.codes.clear();
  }

  public int getCount() {
    return this.get().size();
  }

  public void setTotalCount(int totalCount) {
    this.totalCount = totalCount;
  }
  
  public int getTotalCount() {
    return this.totalCount;
  }

}
