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

package org.monet.space.kernel.constants;

public abstract class LogBookNodeEvent {

	public static final int NONE = 0;
	public static final int DELETED = 1;
	public static final int MODIFIED = 2;
	public static final int MODIFIED_DELETED = 3;
	public static final int CREATED = 4;
	public static final int CREATED_DELETED = 5;
	public static final int CREATED_MODIFIED = 6;
	public static final int CREATED_MODIFIED_DELETED = 7;
	public static final int VISITED = 8;
	public static final int VISITED_DELETED = 9;
	public static final int VISITED_MODIFIED = 10;
	public static final int VISITED_MODIFIED_DELETED = 11;
	public static final int VISITED_CREATED = 12;
	public static final int VISITED_CREATED_DELETED = 13;
	public static final int VISITED_CREATED_MODIFIED = 14;
	public static final int ALL = 15;

}