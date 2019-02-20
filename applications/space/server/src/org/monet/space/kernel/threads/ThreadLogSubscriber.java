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

package org.monet.space.kernel.threads;

import org.monet.space.kernel.library.LibrarySocket;
import org.monet.space.kernel.model.NodeLogBookEntry;
import org.monet.space.kernel.model.ServerConfiguration;

public class ThreadLogSubscriber extends Thread {
	private ServerConfiguration oConfiguration;
	private NodeLogBookEntry oEntry;

	public ThreadLogSubscriber() {
		this.oConfiguration = null;
		this.oEntry = null;
		this.setPriority(Thread.MIN_PRIORITY);
	}

	public Boolean setConfiguration(ServerConfiguration oConfiguration) {
		this.oConfiguration = oConfiguration;
		return true;
	}

	public Boolean setLogEntry(NodeLogBookEntry oEntry) {
		this.oEntry = oEntry;
		return true;
	}

	public void run() {
		LibrarySocket.sendMessage(this.oConfiguration.getHost(), this.oConfiguration.getPort(), this.oEntry.serializeToXML(true).toString());
	}

	public static void sendMessage(ServerConfiguration oConfiguration, NodeLogBookEntry oEntry) {
		ThreadLogSubscriber oThread = new ThreadLogSubscriber();
		oThread.setConfiguration(oConfiguration);
		oThread.setLogEntry(oEntry);
		oThread.run();
	}

}