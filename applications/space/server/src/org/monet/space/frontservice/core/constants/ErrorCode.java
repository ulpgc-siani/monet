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

package org.monet.space.frontservice.core.constants;

public abstract class ErrorCode {
	public static final String ACTIONS_FACTORY = "ERR_ACTIONS_FACTORY";
	public static final String RENDERS_FACTORY = "ERR_RENDERS_FACTORY";
	public static final String USER_NOT_LOGGED = "ERR_USER_NOT_LOGGED";
	public static final String INVALID_INPUT_FORMAT = "ERR_INVALID_INPUT_FORMAT";
	public static final String INVALID_OUTPUT_FORMAT = "ERR_INVALID_OUTPUT_FORMAT";
	public static final String ELEMENT_NOT_FOUND_IN_SERVICE_DEFINITION = "ERR_ELEMENT_NOT_FOUND_IN_SERVICE_DEFINITION";
	public static final String CONTENT_MALFORMED = "ERR_CONTENT_MALFORMED";
	public static final String INCORRECT_PARAMETERS = "ERR_INCORRECT_PARAMETERS";
	public static final String REDIRECT_TO_SETUP = "ERR_REDIRECT_TO_SETUP";
	public static final String INVALID_REQUEST = "ERR_INVALID_REQUEST";
}
