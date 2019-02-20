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

package org.monet.space.setup.control.constants;

public class Actions {
	public static final String SHOW_LOADING = "servletsetup.load";
	public static final String SHOW_APPLICATION = "servletsetup.app";
	public static final String SHOW_BUSINESS_UNIT_STOPPED = "businessunitstopped";
	public static final String SHOW_LOGIN = "showlogin";
	public static final String LOGIN = "login";
	public static final String LOGOUT = "logout";
	public static final String ADD_MASTER_FROM_SIGNATURE = "addmasterfromsignature";
	public static final String ADD_MASTER_FROM_CERTIFICATE = "addmasterfromcertificate";
	public static final String DELETE_MASTER = "deletemaster";
	public static final String START = "start";
	public static final String STOP = "stop";
	public static final String UPLOAD_DISTRIBUTION = "uploaddistribution";
	public static final String DOWNLOAD_DISTRIBUTION = "downloaddistribution";
}
