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

package org.monet.space.mailservice.core.constants;

public abstract class MessageCode {
	public static final String ACCOUNT_SAVED = "MSG_ACCOUNT_SAVED";
	public static final String TRASH_EMPTY = "MSG_TRASH_EMPTY";
	public static final String NODE_SAVED = "MSG_NODE_SAVED";
	public static final String NODES_ATTRIBUTE_SAVED = "MSG_NODES_ATTRIBUTE_SAVED";
	public static final String NODE_REMOVED = "MSG_NODE_REMOVED";
	public static final String NODE_IMPORTED = "MSG_NODE_IMPORTED";
	public static final String NODE_MADE_PUBLIC = "MSG_MADE_PUBLIC";
	public static final String NODE_MADE_PRIVATE = "MSG_MADE_PRIVATE";
	public static final String NODE_COMMAND_EXECUTED = "MSG_NODE_COMMAND_EXECUTED";
	public static final String TASK_SAVED = "MSG_TASK_SAVED";
	public static final String TASK_REMOVED = "MSG_TASK_REMOVED";
	public static final String SUBSCRIBED = "MSG_SUBSCRIBED";
}
