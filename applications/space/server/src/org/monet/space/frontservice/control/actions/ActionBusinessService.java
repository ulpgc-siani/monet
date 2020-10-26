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

package org.monet.space.frontservice.control.actions;

import com.sun.media.jfxmedia.logging.Logger;
import org.monet.metamodel.TaskDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.http.Request;
import org.monet.space.frontservice.control.constants.Parameter;
import org.monet.space.frontservice.ApplicationFrontService;
import org.monet.space.frontservice.core.constants.ErrorCode;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.AgentNotifier;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.components.layers.ServiceLayer;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.exceptions.CantSignException;
import org.monet.space.kernel.exceptions.SessionException;
import org.monet.space.kernel.guice.InjectorFactory;
import org.monet.space.kernel.library.LibrarySigner;
import org.monet.space.kernel.machines.ttm.CourierService;
import org.monet.space.kernel.machines.ttm.Engine;
import org.monet.space.kernel.machines.ttm.behavior.CustomerBehavior;
import org.monet.space.kernel.machines.ttm.model.MailBox;
import org.monet.space.kernel.machines.ttm.model.MailBox.Type;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.threads.MonetSystemThread;

import java.net.URLDecoder;
import java.util.Date;

public class ActionBusinessService extends Action {
	private TaskLayer taskLayer;
	private ServiceLayer serviceLayer;
	private CourierService courierService;

	public ActionBusinessService() {
		super();

		ComponentPersistence componentPersistence = ComponentPersistence.getInstance();
		this.taskLayer = componentPersistence.getTaskLayer();
		this.serviceLayer = componentPersistence.getServiceLayer();

		this.courierService = InjectorFactory.getInstance().getInstance(CourierService.class);
	}

	private Task createTask(TaskDefinition taskDefinition) {
		Task task = this.taskLayer.createTask(taskDefinition.getCode());
		Ref roleRef = taskDefinition.getRole();
		String roleCode = null;

		if (roleRef != null)
			roleCode = Dictionary.getInstance().getDefinitionCode(roleRef.getValue());

		task.setRole(roleCode);
		task.setLabel(taskDefinition.getLabelString());
		task.setDescription(taskDefinition.getDescriptionString());
		task.setEndSuggestedDate(null);

		return task;
	}

	public String execute() {
		Configuration configuration = Configuration.getInstance();
		String sourceUnit = ((String) this.parameters.get(Parameter.SOURCE_UNIT)).trim();
		String serviceName = ((String) this.parameters.get(Parameter.SERVICE_NAME)).trim();
		String replyMailBox = ((String) this.parameters.get(Parameter.REPLY_MAILBOX)).trim();
		String parameterSuggestedStartDate = ((String) this.parameters.get(Parameter.START_DATE)).trim();
		Date suggestedStartDate = parameterSuggestedStartDate.isEmpty() ? null : new Date(Long.valueOf(parameterSuggestedStartDate));
		String parameterSuggestedEndDate = ((String) this.parameters.get(Parameter.END_DATE)).trim();
		Date suggestedEndDate = parameterSuggestedEndDate.isEmpty() ? null : new Date(Long.valueOf(parameterSuggestedEndDate));
		String comments = decode(((String) this.parameters.get(Parameter.COMMENTS)).trim());
		boolean urgent = Boolean.parseBoolean(((String) this.parameters.get(Parameter.URGENT)).trim());
		TaskDefinition taskDefinition;
		Task task = null;
		Service service = null;
		Dictionary dictionary = Dictionary.getInstance();

		if (!dictionary.existsDefinition(serviceName))
			throw new RuntimeException("BUSINESS_SERVICE_NOT_FOUND");

		taskDefinition = dictionary.getTaskDefinitionByServiceName(serviceName);

		if (replyMailBox == null)
			throw new SessionException(ErrorCode.INCORRECT_PARAMETERS, Parameter.REPLY_MAILBOX);

		AgentLogger.getInstance().info("Service request with unit: " + sourceUnit + ", service: " + serviceName + " and reply mailbox: " + replyMailBox);

		try {

			try {
				comments = URLDecoder.decode(comments, "UTF-8");
			}
			catch (Exception exception) {
			}

			task = this.createTask(taskDefinition);
			final String taskId = task.getId();
			task.setStartSuggestedDate(suggestedStartDate);
			task.setEndSuggestedDate(suggestedEndDate);
			task.setComments(comments);
			task.setUrgent(urgent);
			task.setPartnerContext(sourceUnit);
			this.taskLayer.saveTask(task);

			User user = Context.getInstance().getCurrentSession().getAccount().getUser();
			MailBoxUri replyMailBoxUri = MailBoxUri.build(replyMailBox);

			CustomerBehavior customer = task.getProcess().getCustomer();
			MailBox localMailBox = this.courierService.createMailBox(task.getCode(), taskId, Type.CUSTOMER, user);
			customer.initialize(localMailBox.toUri(), replyMailBoxUri, suggestedStartDate, suggestedEndDate, comments, urgent, sourceUnit);

			service = this.serviceLayer.createService(taskDefinition.getCode());
			service.setReplyToMailBox(replyMailBoxUri);
			service.setTaskId(taskId);
			service.setLocalMailBox(localMailBox.getId());
			service.setRemoteUnitLabel(user.getName());
			this.serviceLayer.saveService(service);

			AgentNotifier.getInstance().notify(new MonetEvent(MonetEvent.SERVICE_STARTED, null, service));

			MonetSystemThread thread = new MonetSystemThread(new Runnable() {
				public void run() {
					Engine engine = InjectorFactory.getInstance().getInstance(Engine.class);
					engine.getProcess(taskId).resume();
				}

				;
			});
			thread.start();

            String spaceName = BusinessUnit.getInstance().getName();
            String mailBoxUri = MailBoxUri.build(spaceName, localMailBox.getId()).toString();

			try {
				String signature = LibrarySigner.signText(mailBoxUri, configuration.getCertificateFilename(), configuration.getCertificatePassword());
				return String.format("mailbox=%s&signature=%s", mailBoxUri, signature);
			} catch (Exception exception) {
				throw new CantSignException(String.format("Could not sign message: %s with certificate: %s", mailBoxUri, configuration.getCertificateFilename()));
			}

		} catch (Throwable e) {
			AgentLogger.getInstance().error("Error in service request with unit: " + sourceUnit + ", service: " + serviceName + " and reply mailbox: " + replyMailBox, e);
			if (task != null) {
				try {
					this.taskLayer.deleteTask(task.getId());
				} catch (Exception ex) {
				}
			}
			if (service != null) {
				try {
					this.serviceLayer.removeService(service.getId());
				} catch (Exception ex) {
				}
			}
			throw new RuntimeException(e);
		}
	}

	protected FederationLayer getFederationLayer() {
		return ComponentFederation.getInstance().getLayer(new FederationLayer.Configuration() {
			@Override
			public String getSessionId() {
				return request.getSessionId();
			}

			@Override
			public String getCallbackUrl() {
				return ApplicationFrontService.getCallbackUrl();
			}

			@Override
			public String getLogoUrl() {
				return null;
			}

			@Override
			public Request getRequest() {
				return request;
			}
		});
	}

}