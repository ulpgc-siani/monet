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

package org.monet.space.kernel.components.monet;

import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.*;
import org.monet.space.kernel.components.monet.layers.*;
import org.monet.space.kernel.exceptions.SystemException;

import java.util.HashMap;

public class ComponentPersistenceMonet extends ComponentPersistence {

	public static final String NAME = "nodesmonet";

	private final EventLayer eventLayer;
	private final NewsLayer newsLayer;
	private final NodeLayer nodeLayer;
	private final TaskLayer taskLayer;
	private final TeamLayer teamLayer;
	private final SourceLayer sourceLayer;
	private final HistoryStoreLayer historyStoreLayer;
	private final ServiceLayer serviceLayer;
	private final NotificationLayer notificationLayer;
	private final EventLogLayer eventLogLayer;
	private final MailBoxLayer mailBoxLayer;
	private final MessageQueueLayer messageQueueLayer;

	private ComponentPersistenceMonet() {
		super();
		this.eventLayer = new EventLayerMonet(this);
		this.newsLayer = new NewsLayerMonet(this);
		this.nodeLayer = new NodeLayerMonet(this);
		this.taskLayer = new TaskLayerMonet(this);
		this.teamLayer = new TeamLayerMonet(this);
		this.sourceLayer = new SourceLayerMonet(this);
		this.historyStoreLayer = new HistoryStoreLayerMonet(this);
		this.serviceLayer = new ServiceLayerMonet(this);
		this.notificationLayer = new NotificationLayerMonet(this);
		this.eventLogLayer = new EventLogLayerMonet(this);
		this.mailBoxLayer = new MailBoxLayerMonet(this);
		this.messageQueueLayer = new MessageQueueLayerMonet(this);
	}

	public synchronized static ComponentPersistence getInstance() {
		if (instance == null) instance = new ComponentPersistenceMonet();
		return instance;
	}

	@Override
	public HashMap<Integer, Boolean> getSupportedFeatures() {
		return new HashMap<Integer, Boolean>();
	}

	@Override
	public void run() throws SystemException {
		if (this.isRunning) return;
		this.isRunning = true;
		getNodeLayer().resetYearSequences();
	}

	@Override
	public void stop() throws SystemException {
		if (!this.isRunning) return;
		instance = null;
		this.isRunning = false;
	}

	@Override
	public EventLayer getEventLayer() {
		return this.eventLayer;
	}

	@Override
	public NewsLayer getNewsLayer() {
		return this.newsLayer;
	}

	@Override
	public NodeLayer getNodeLayer() {
		return this.nodeLayer;
	}

	@Override
	public TaskLayer getTaskLayer() {
		return taskLayer;
	}

	@Override
	public TeamLayer getTeamLayer() {
		return teamLayer;
	}

	@Override
	public SourceLayer getSourceLayer() {
		return this.sourceLayer;
	}

	@Override
	public HistoryStoreLayer getHistoryStoreLayer() {
		return this.historyStoreLayer;
	}

	@Override
	public ServiceLayer getServiceLayer() {
		return this.serviceLayer;
	}

	@Override
	public NotificationLayer getNotificationLayer() {
		return this.notificationLayer;
	}

	@Override
	public EventLogLayer getEventLogLayer() {
		return this.eventLogLayer;
	}

	@Override
	public MailBoxLayer getMailBoxLayer() {
		return this.mailBoxLayer;
	}

	@Override
	public MessageQueueLayer getMessageQueueLayer() {
		return this.messageQueueLayer;
	}

	@Override
	public void reset() {
		this.newsLayer.reset();
		this.nodeLayer.reset();
		this.taskLayer.reset();
		this.teamLayer.reset();
		this.sourceLayer.reset();
		this.historyStoreLayer.reset();
		this.serviceLayer.reset();
		this.notificationLayer.reset();
		this.eventLogLayer.reset();
		this.mailBoxLayer.reset();
		this.messageQueueLayer.reset();
	}

	@Override
	public void create() {
		this.newsLayer.create();
		this.nodeLayer.create();
		this.taskLayer.create();
		this.teamLayer.create();
		this.sourceLayer.create();
		this.historyStoreLayer.create();
		this.serviceLayer.create();
		this.notificationLayer.create();
		this.eventLogLayer.create();
		this.mailBoxLayer.create();
		this.messageQueueLayer.create();
	}

	@Override
	public void destroy() {
		this.newsLayer.destroy();
		this.nodeLayer.destroy();
		this.taskLayer.destroy();
		this.teamLayer.destroy();
		this.sourceLayer.destroy();
		this.historyStoreLayer.destroy();
		this.serviceLayer.destroy();
		this.notificationLayer.destroy();
		this.eventLogLayer.destroy();
		this.mailBoxLayer.destroy();
		this.messageQueueLayer.destroy();
	}

}