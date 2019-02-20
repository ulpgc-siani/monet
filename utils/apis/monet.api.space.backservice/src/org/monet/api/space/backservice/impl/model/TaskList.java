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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskList extends BaseModelList<Task> {

  public TaskList() {
    super();
  }

  public void add(Task baseModel) {
    String id;
    
    id = baseModel.getId();
    if ((id.equals("-1")) || (id.isEmpty())) id = String.valueOf(this.items.size());
    
    this.items.put(id, baseModel);
  }
  
  public void removeOfType(String taskType) {
    Iterator<String> iterator = this.items.keySet().iterator();
    ArrayList<String> ids = new ArrayList<String>();
    
    while (iterator.hasNext()) {
      String id = iterator.next();
      Task task = this.items.get(id);
      if (task.getType().equals(taskType)) ids.add(id);
    }
    
    iterator = ids.iterator();
    while (iterator.hasNext()) this.items.remove(iterator.next());
    
  }
  
  @SuppressWarnings("unchecked")
  public void deserializeFromXML(Element taskList) throws ParseException {
    List<Element> tasks;
    Iterator<Element> iterator;
    
    if (taskList == null) return;

    tasks = taskList.getChildren("task");
    iterator = tasks.iterator();

    this.clear();

    while (iterator.hasNext()) {
      Task task = new Task();
      task.deserializeFromXML(iterator.next());
      this.add(task);
    }
   
  }

}
