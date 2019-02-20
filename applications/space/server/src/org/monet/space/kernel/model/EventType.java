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

public final class EventType {

	public static final int NONE = 0;
	public static final int DELETE = 1;
	public static final int MODIFY = 2;
	public static final int MODIFY_DELETE = 3;
	public static final int CREATE = 4;
	public static final int CREATE_DELETE = 5;
	public static final int CREATE_MODIFY = 6;
	public static final int CREATE_MODIFY_DELETE = 7;
	public static final int ACCESS = 8;
	public static final int ACCESS_DELETE = 9;
	public static final int ACCESS_MODIFY = 10;
	public static final int ACCESS_MODIFY_DELETE = 11;
	public static final int ACCESS_CREATE = 12;
	public static final int ACCESS_CREATE_DELETE = 13;
	public static final int ACCESS_CREATE_MODIFY = 14;
	public static final int ALL = 15;

}
