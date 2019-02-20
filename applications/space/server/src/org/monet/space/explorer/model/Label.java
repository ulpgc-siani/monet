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

package org.monet.space.explorer.model;

public interface Label {
	public static final String USER_NOT_GRANTED = "user-not-granted";
	public static final String LOGOUT = "logout";
    public static final String ALL_RIGHTS_RESERVED = "all-rights-reserved";
    public static final String UPDATING_SPACE = "updating-space";
    public static final String RELOAD = "reload";
    public static final String LOGIN = "login";
    public static final String WELCOME = "welcome";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String ACCEPT = "accept";
}
