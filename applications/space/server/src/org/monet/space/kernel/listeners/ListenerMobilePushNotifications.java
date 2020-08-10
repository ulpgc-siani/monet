package org.monet.space.kernel.listeners;

import org.monet.metamodel.RoleDefinition;
import org.monet.metamodel.SendJobActionProperty;
import org.monet.metamodel.SourceDefinition;
import org.monet.metamodel.TaskDefinition;
import org.monet.mobile.service.PushOperations;
import org.monet.space.kernel.agents.AgentMobilePushService;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.RoleLayer;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListenerMobilePushNotifications extends Listener {

	private void addBusinessUnitUID(Map<String, String> parameters) {
		BusinessUnit businessUnit = BusinessUnit.getInstance();
		parameters.put("business_unit_uid", businessUnit.getFederation().getUri() + businessUnit.getName());
	}

	@Override
	public void modelUpdated(MonetEvent event) {
		Map<String, String> parameters = new HashMap<>();
		addBusinessUnitUID(parameters);
		AgentMobilePushService.getInstance().pushToAll(PushOperations.DEFINITION_UPDATE, parameters);
	}

	@Override
	public void sourceSynchronized(MonetEvent event) {
		Source<SourceDefinition> source = (Source<SourceDefinition>) event.getSender();
		Map<String, String> parameters = new HashMap<>();
		parameters.put("id", source.getId());
		addBusinessUnitUID(parameters);
		AgentMobilePushService.getInstance().pushToAll(PushOperations.GLOSSARY_UPDATE, parameters);
	}

	@Override
	public void taskJobCreated(MonetEvent event) {
		String userId = event.getParameter(MonetEvent.PARAMETER_USER_ID);
		Map<String, String> parameters = new HashMap<>();
		parameters.put("id", (String) event.getSender());
		addBusinessUnitUID(parameters);
		AgentMobilePushService.getInstance().push(userId, PushOperations.TASK_UPDATE, parameters);
	}

	@Override
	public void taskJobUnassigned(MonetEvent event) {
		String userId = event.getParameter(MonetEvent.PARAMETER_USER_ID);
		Map<String, String> parameters = new HashMap<>();
		parameters.put("id", (String) event.getSender());
		addBusinessUnitUID(parameters);
		AgentMobilePushService.getInstance().push(userId, PushOperations.TASK_UPDATE, parameters);
	}

	@Override
	public void taskFinished(MonetEvent event) {
		Task task = (Task) event.getSender();

		Map<String, String> parameters = new HashMap<>();
		parameters.put("id", task.getId());
		addBusinessUnitUID(parameters);

		if (task.isJob())
			AgentMobilePushService.getInstance().push(task.getOwnerId(), PushOperations.TASK_UPDATE, parameters);
	}

	@Override
	public void taskNewMessagesSent(MonetEvent event) {
		String taskOrderId = event.getParameter(MonetEvent.PARAMETER_ORDER_ID);
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		TaskOrder taskOrder = taskLayer.loadTaskOrder(taskOrderId);

		if (taskOrder.getType() == TaskOrder.Type.job) {
			List<String> usersIds = new ArrayList<>();
			String userId = taskLayer.getJobUserIdForTaskOrder(taskOrderId);

			if (userId == null)
				usersIds = this.loadJobUsersForBroadcasting(taskOrder);
			else
				usersIds.add(userId);

			Map<String, String> parameters = new HashMap<>();
			parameters.put("id", ((Task) event.getSender()).getId());
			addBusinessUnitUID(parameters);

			for (String user : usersIds)
				AgentMobilePushService.getInstance().push(user, PushOperations.CHAT_UPDATE, parameters);
		}
	}

	private List<String> loadJobUsersForBroadcasting(TaskOrder taskOrder) {
		List<String> usersIds = new ArrayList<>();
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		RoleLayer roleLayer = ComponentFederation.getInstance().getRoleLayer();

		Task task = taskLayer.loadTask(taskOrder.getTaskId());
		TaskDefinition taskDefinition = task.getDefinition();
		SendJobActionProperty sendJobActionDefinition = taskDefinition.getSendJobAction(taskOrder.getCode());

		if (sendJobActionDefinition == null)
			return usersIds;

		DataRequest dataRequest = new DataRequest();
		dataRequest.setStartPos(0);
		dataRequest.setLimit(-1);
		dataRequest.addParameter(DataRequest.NATURE, Role.Nature.Internal.toString());
		dataRequest.addParameter(DataRequest.NON_EXPIRED, "true");

		RoleDefinition roleDefinition = Dictionary.getInstance().getRoleDefinition(sendJobActionDefinition.getRole().getValue());
		dataRequest.setCode(roleDefinition.getCode());
		RoleList roleList = roleLayer.loadRoleList(dataRequest);
		for (Role roleInstance : roleList.get().values()) {
			if (!roleInstance.isUserRole()) continue;

			String userId = ((UserRole) roleInstance).getUserId();
			if (usersIds.contains(userId)) continue;

			usersIds.add(userId);
		}

		return usersIds;
	}

}
