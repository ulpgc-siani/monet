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

package org.monet.space.kernel.producers;

import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.exceptions.SystemException;

import java.util.HashMap;

public class ProducersFactory {

	private static ProducersFactory instance;
	private HashMap<String, Class<?>> hmProducers;
	private HashMap<String, Object> hmProducersInstances;

	private ProducersFactory() {
		this.hmProducers = new HashMap<String, Class<?>>();
		this.hmProducersInstances = new HashMap<String, Object>();
		this.registerProducers();
	}

	private Boolean registerProducers() {

		this.register(Producers.FEDERATION, ProducerFederation.class);
		this.register(Producers.FEDERATIONLIST, ProducerFederationList.class);
		this.register(Producers.ATTRIBUTE, ProducerAttribute.class);
		this.register(Producers.ATTRIBUTELIST, ProducerAttributeList.class);
		this.register(Producers.INDICATOR, ProducerIndicator.class);
		this.register(Producers.INDICATORLIST, ProducerIndicatorList.class);
		this.register(Producers.NODE, ProducerNode.class);
		this.register(Producers.NODELIST, ProducerNodeList.class);
		this.register(Producers.REFERENCE, ProducerReference.class);
		this.register(Producers.REFERENCELIST, ProducerReferenceList.class);
		this.register(Producers.TASK, ProducerTask.class);
		this.register(Producers.TASKJOB, ProducerTaskJob.class);
		this.register(Producers.TASKORDER, ProducerTaskOrder.class);
		this.register(Producers.TASKORDERLIST, ProducerTaskOrderList.class);
		this.register(Producers.TASKLIST, ProducerTaskList.class);
		this.register(Producers.TEAM, ProducerTeam.class);
		this.register(Producers.LOGBOOKNODE, ProducerLogBookNode.class);
		this.register(Producers.FACTBOOKTASK, ProducerFactTask.class);
		this.register(Producers.SOURCE, ProducerSource.class);
		this.register(Producers.SOURCELIST, ProducerSourceList.class);
		this.register(Producers.SOURCESTORE, ProducerSourceStore.class);
		this.register(Producers.HISTORY_STORE, ProducerHistoryStore.class);
		this.register(Producers.DATALINK, ProducerDataLink.class);
		this.register(Producers.SEQUENCE, ProducerSequence.class);
		this.register(Producers.SERVICE, ProducerService.class);
		this.register(Producers.SERVICE, ProducerService.class);
		this.register(Producers.EVENTLOG, ProducerEventLog.class);
		this.register(Producers.REPORTLIST, ProducerReportList.class);
		this.register(Producers.REPORT, ProducerReport.class);
		this.register(Producers.REVISION, ProducerNodeRevision.class);
		this.register(Producers.REVISIONLIST, ProducerNodeRevisionList.class);
		this.register(Producers.NOTIFICATION, ProducerNotification.class);
		this.register(Producers.NOTIFICATIONLIST, ProducerNotificationList.class);
		this.register(Producers.WORKQUEUE, ProducerWorkQueue.class);
		this.register(Producers.LOCATION, ProducerLocation.class);
		this.register(Producers.LOCATIONLIST, ProducerLocationList.class);
		this.register(Producers.POST, ProducerPost.class);
		this.register(Producers.POSTLIST, ProducerPostList.class);
		this.register(Producers.FILTERGROUP, ProducerFilterGroup.class);
		this.register(Producers.FILTERGROUPLIST, ProducerFilterGroupList.class);
		this.register(Producers.ROLELIST, ProducerRoleList.class);
		this.register(Producers.ROLE, ProducerRole.class);
		this.register(Producers.MAILBOX, ProducerMailBox.class);
		this.register(Producers.MESSAGEQUEUE, ProducerMessageQueue.class);
		this.register(Producers.MASTER, ProducerMaster.class);
		this.register(Producers.MASTERLIST, ProducerMasterList.class);
		this.register(Producers.EVENT, ProducerEvent.class);
		this.register(Producers.EVENTLIST, ProducerEventList.class);
		this.register(Producers.DATASTORE, ProducerDatastore.class);

		return true;
	}

	public synchronized static ProducersFactory getInstance() {
		if (instance == null)
			instance = new ProducersFactory();
		return instance;
	}

	public <T extends Producer> T get(String sType) {
		Class<?> producerClass;
		Producer producer;

		try {
			producer = (Producer) this.hmProducersInstances.get(sType);
			if (producer == null) {
				producerClass = this.hmProducers.get(sType);
				producer = (Producer) producerClass.newInstance();
				this.hmProducersInstances.put(sType, producer);
			}
		} catch (NullPointerException oException) {
			throw new SystemException(ErrorCode.PRODUCERS_FACTORY, sType, oException);
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.PRODUCERS_FACTORY, sType, oException);
		}

		return (T) producer;
	}

	public Boolean register(String sType, Class<?> cProducer) throws IllegalArgumentException {

		if ((cProducer == null) || (sType == null)) {
			return false;
		}
		this.hmProducers.put(sType, cProducer);

		return true;
	}

}
