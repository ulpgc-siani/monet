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

package org.monet.space.kernel.model;

import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;

public class TaskProvider implements TaskLink {
	private TaskLayer taskLayer;

	private static TaskProvider istance;

	private TaskProvider() {
		this.taskLayer = ComponentPersistence.getInstance().getTaskLayer();
	}

	public synchronized static TaskProvider getInstance() {
		if (istance == null) istance = new TaskProvider();
		return istance;
	}

	@Override
	public TaskList loadTasks(Account account, DataRequest dataRequest) {
		return this.taskLayer.loadTasks(account, dataRequest);
	}

	@Override
	public Integer loadTasksCount(Account account, DataRequest dataRequest) {
		return this.taskLayer.loadTasksCount(account, dataRequest);
	}

	@Override
	public TaskList searchTasks(Account account, TaskSearchRequest searchRequest) {
		return this.taskLayer.searchTasks(account, searchRequest);
	}

	@Override
	public Integer searchTasksCount(Account account, TaskSearchRequest searchRequest) {
		return this.taskLayer.searchTasksCount(account, searchRequest);
	}

}