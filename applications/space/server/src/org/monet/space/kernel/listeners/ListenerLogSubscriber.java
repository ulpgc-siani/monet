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

package org.monet.space.kernel.listeners;

import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.constants.ModelProperty;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.threads.ThreadLogSubscriber;

public class ListenerLogSubscriber extends Listener {

	public ListenerLogSubscriber() {
	}

	private void sendMessageToSubscribers(String idNode, Integer iType) {
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		LogSubscriberList subscriberList = nodeLayer.loadNodeSubscribers(iType);
		NodeLogBookEntry entry = new NodeLogBookEntry(idNode, iType);

		for (LogSubscriber subscriber : subscriberList) {
			if ((subscriber.getType() & iType) == 0) continue;
			ThreadLogSubscriber.sendMessage(subscriber.getServerConfiguration(), entry);
		}
	}

	@Override
	public void nodeVisited(MonetEvent event) {
//    DISABLED 
//     Node node = (Node)event.getSender();
//     this.sendMessageToSubscribers(node.getId(), 8);
	}

	@Override
	public void nodeCreated(MonetEvent event) {
		Node node = (Node) event.getSender();
		this.sendMessageToSubscribers(node.getId(), 4);
	}

	@Override
	public void nodeModified(MonetEvent event) {
		Node node = (Node) event.getSender();

		if (event.getProperty().equals(ModelProperty.REFERENCE)) return;

		this.sendMessageToSubscribers(node.getId(), 2);
	}

	@Override
	public void nodeRemoved(MonetEvent event) {
		Node node = (Node) event.getSender();
		this.sendMessageToSubscribers(node.getId(), 1);
	}

	@Override
	public void nodeMovedToTrash(MonetEvent event) {
		Node node = (Node) event.getSender();
		this.sendMessageToSubscribers(node.getId(), 1);
	}

	@Override
	public void nodeRecoveredFromTrash(MonetEvent event) {
		Node node = (Node) event.getSender();
		this.sendMessageToSubscribers(node.getId(), 4);
	}

}