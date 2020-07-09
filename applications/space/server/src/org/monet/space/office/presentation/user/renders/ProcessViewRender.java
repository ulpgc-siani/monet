package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.*;
import org.monet.metamodel.EditionActionProperty.UseProperty;
import org.monet.metamodel.LineActionPropertyBase.LineStopProperty;
import org.monet.metamodel.LineActionPropertyBase.TimeoutProperty;
import org.monet.space.kernel.library.LibraryDate;
import org.monet.space.kernel.machines.ttm.behavior.ProcessBehavior;
import org.monet.space.kernel.machines.ttm.behavior.ProviderBehavior;
import org.monet.space.kernel.model.*;
import org.monet.space.office.core.model.Language;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProcessViewRender extends TaskViewRender {

	public ProcessViewRender() {
		super();
	}

	private String initNodeView(String type, Node node, String nodeView) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		OfficeRender render;

		if (node == null)
			return "";

		if (nodeView == null)
			nodeView = this.getDefaultWidgetView(node);

		if (nodeView.isEmpty())
			return "";

		map.put("idTask", this.task.getId());
		map.put("idNode", node != null ? node.getId() : "");

		render = this.rendersFactory.get(node, "preview.html", this.renderLink, account);
		render.setParameter("view", nodeView);
		render.setParameter("idEntityContainer", this.task.getId());
		map.put("render(node.view)", render.getOutput());

		if (node != null && node.isDocument())
			type += ".document";

		return block("content." + type, map);
	}

	private String initStateViewDelegation(DelegationActionProperty delegationDefinition, String message) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		StringBuffer roleListBuffer = new StringBuffer();
		String setup = "";
		String requireConfirmation = requireConfirmationMessage(delegationDefinition);

		if (message == null) {
			if (delegationDefinition.getMode() == null || delegationDefinition.getMode().equals(DelegationActionProperty.ModeEnumeration.SELECT)) {
				ProcessDefinition definition = (ProcessDefinition) this.definition;
				String providerKey = delegationDefinition.getProvider().getValue();
				TaskProviderProperty providerDefinition = definition.getTaskProviderPropertyMap().get(providerKey);
				String roleKey = providerDefinition.getRole().getValue();
				RoleDefinition roleDefinition = this.dictionary.getRoleDefinition(roleKey);
				Role.Nature nature;
				String orderId = null;
				ProviderBehavior currentProvider = this.task.getProcess().getCurrentProvider();

				if (currentProvider != null)
					orderId = currentProvider.getModel().getOrderId();

				TaskOrder order = orderId != null ? this.renderLink.loadTaskOrder(orderId) : null;
				if (order == null || order.getRole() == null) {
					if (providerDefinition.getExternal() != null && providerDefinition.getInternal() != null)
						nature = Role.Nature.Both;
					else if (providerDefinition.getExternal() != null)
						nature = Role.Nature.External;
					else if (providerDefinition.getInternal() != null)
						nature = Role.Nature.Internal;
					else
						nature = null;

					if (nature == null)
						message = block("content.state$current$action.delegation$message.unknown", map);

					RoleList roleList = this.renderLink.loadNonExpiredRoleList(roleDefinition.getCode(), nature);
					if (roleList.getTotalCount() <= 0) {
						map.put("role", this.language.getModelResource(roleDefinition.getLabel()));
						message = block("content.state$current$action.delegation$message.empty", map);
					} else {
						for (Role role : roleList.get().values()) {
							HashMap<String, Object> localMap = new HashMap<String, Object>();
							String name = role.getLabel();

							localMap.put("taskId", this.task.getId());
							localMap.put("roleId", role.getId());
							localMap.put("name", name);
							localMap.put("requireConfirmation", requireConfirmation);
							roleListBuffer.append(block("content.state$current$action.delegation$role", localMap));
						}

						if (message == null)
							message = block("content.state$current$action.delegation$message", map);
					}
				} else if (order.getSetupNodeId() != null) {
					Node setupNode = this.renderLink.loadNode(order.getSetupNodeId());
					Role role = order.getRole();
					String roleValue = role.getLabel();

					map.put("role", roleValue);
					message = block("content.state$current$action.delegation$message.setup", map);

					if (setupNode == null)
						setup = block("content.state$current$action.delegation.waiting", map);
					else {
						OfficeRender render = this.rendersFactory.get(setupNode, "edit.html", this.renderLink, account);
						render.setParameter("view", this.getDefaultWidgetView(setupNode));
						map.put("render(node.form)", render.getOutput());
						map.put("idTask", this.task.getId());
						map.put("requireConfirmation", requireConfirmation);
						setup = block("content.state$current$action.delegation$setup", map);
					}
				}
			}
		}

		map.put("message", message);
		map.put("roleList", roleListBuffer.toString());
		map.put("setup", setup);

		return block("content.state$current$action.delegation", map);
	}

	private String initStateViewDelegationOnFailure(DelegationActionProperty delegationDefinition) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		ProcessDefinition definition = (ProcessDefinition) this.definition;
		String providerKey = delegationDefinition.getProvider().getValue();
		TaskProviderProperty providerDefinition = definition.getTaskProviderPropertyMap().get(providerKey);
		String orderId = this.task.getProcess().getCurrentProvider().getModel().getOrderId();
		TaskOrder order = this.renderLink.loadTaskOrder(orderId);
		Role role = order.getRole();

		Date failureDate = this.task.getProcess().getCurrentProvider().getModel().getFailureDate();
		map.put("date", LibraryDate.getDateAndTimeString(failureDate, org.monet.space.office.core.model.Language.getCurrent(), Language.getCurrentTimeZone(), LibraryDate.Format.INTERNAL, true, "/"));

		String blockName = "content.state$current$action.delegation$message.failure";
		if (role != null && role.isUserRole() && providerDefinition.getInternal() == null) {
			blockName = "content.state$current$action.delegation$message.failure.internal";
			order.setRole(null);
		}
		if (role != null && (role.isServiceRole() || role.isFeederRole()) && providerDefinition.getExternal() == null) {
			blockName = "content.state$current$action.delegation$message.failure.external";
			order.setRole(null);
		}

		return this.initStateViewDelegation(delegationDefinition, block(blockName, map));
	}

	private String initStateViewSendJob(SendJobActionProperty sendJobDefinition) {
		HashMap<String, Object> map = new HashMap<>();
		StringBuffer roleListBuffer = new StringBuffer();
		String setup = "";
		String message = "";
		String requireConfirmation = requireConfirmationMessage(sendJobDefinition);

		if (sendJobDefinition.getMode() == null || sendJobDefinition.getMode().equals(DelegationActionProperty.ModeEnumeration.SELECT)) {
			RoleDefinition roleDefinition = this.dictionary.getRoleDefinition(sendJobDefinition.getRole().getValue());
			String orderId = this.task.getProcess().getCurrentJobOrderId();
			TaskOrder order = orderId != null ? this.renderLink.loadTaskOrder(orderId) : null;

			if (order == null || order.getRole() == null) {

				RoleList roleList = this.renderLink.loadNonExpiredRoleList(roleDefinition.getCode(), Role.Nature.Internal);
				HashMap<String, Object> localMap = new HashMap<String, Object>();
				localMap.put("taskId", this.task.getId());
				localMap.put("requireConfirmation", requireConfirmation);

				for (Role role : roleList.get().values()) {
					String name = role.getLabel();

					localMap.put("roleId", role.getId());
					localMap.put("name", name);
					roleListBuffer.append(block("content.state$current$action.sendjob$role", localMap));
				}
				roleListBuffer.append(block("content.state$current$action.sendjob$role.unassign", localMap));

				message = block("content.state$current$action.sendjob$message", map);
			} else {
				Role role = order.getRole();
				String blockName = "content.state$current$action.sendjob$message.setup.unassign";

				if (role != null) {
					String roleValue = role.getLabel();
					map.put("role", roleValue);
					blockName = "content.state$current$action.sendjob$message.setup";
				}

				message = block(blockName, map);

				if (order.getSetupNodeId() == null)
					setup = block("content.state$current$action.sendjob.waiting", map);
				else {
					Node setupNode = this.renderLink.loadNode(order.getSetupNodeId());
					OfficeRender render = this.rendersFactory.get(setupNode, "edit.html", this.renderLink, account);
					render.setParameter("view", this.getDefaultWidgetView(setupNode));
					map.put("render(node.form)", render.getOutput());
					map.put("idTask", this.task.getId());
					map.put("requireConfirmation", requireConfirmation);
					setup = block("content.state$current$action.sendjob$setup", map);
				}
			}
		}

		map.put("message", message);
		map.put("roleList", roleListBuffer.toString());
		map.put("setup", setup);

		return block("content.state$current$action.sendjob", map);
	}

	private String initStateViewLine(PlaceProperty placeDefinition, LineActionProperty lineDefinition) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		StringBuffer stopListBuffer = new StringBuffer();
		String timeout = "";
		String requireConfirmation = requireConfirmationMessage(lineDefinition);

		map.put("title", this.language.getModelResource(lineDefinition.getLabel()));

		if (lineDefinition.getTimeout() != null) {
			TimeoutProperty timeoutDefinition = lineDefinition.getTimeout();
			HashMap<String, Object> localMap = new HashMap<String, Object>();
			String takeStopKey = timeoutDefinition.getTake().getValue();
			LineStopProperty takeStop = lineDefinition.getStopMap().get(takeStopKey);
			Long dueStamp = this.task.getProcess().getTimerDue(placeDefinition.getCode());

			if (dueStamp != -1) {
				Date dueDate = new Date();
				dueDate.setTime(dueStamp);

				localMap.put("option", this.language.getModelResource(takeStop.getLabel()));
				localMap.put("timeoutDate", LibraryDate.getDateAndTimeString(dueDate, Language.getCurrent(), Language.getCurrentTimeZone(), LibraryDate.Format.INTERNAL, true, "/"));

				timeout = block("content.state$current$action.line$timeout", localMap);
			} else
				timeout = "";
		}

		for (LineStopProperty lineStop : lineDefinition.getStopList()) {
			if (lineStop.isHidden())
				continue;

			HashMap<String, Object> localMap = new HashMap<String, Object>();
			localMap.put("taskId", this.task.getId());
			localMap.put("placeCode", placeDefinition.getCode());
			localMap.put("stopCode", lineStop.getCode());
			localMap.put("label", this.language.getModelResource(lineStop.getLabel()));
			localMap.put("requireConfirmation", requireConfirmation);
			stopListBuffer.append(block("content.state$current$action.line$stop", localMap));
		}

		map.put("timeout", timeout);
		map.put("stopList", stopListBuffer.toString());

		return block("content.state$current$action.line", map);
	}

	private String initStateViewEdition(PlaceProperty placeDefinition, EditionActionProperty editionDefinition) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String idForm = this.task.getProcess().getEditionFormId();
		UseProperty useDefinition = editionDefinition.getUse();
		FormDefinition formDefinition = this.dictionary.getFormDefinition(useDefinition.getForm().getValue());
		Node form;
		OfficeRender render;
		String renderResult = "";
		String requireConfirmation = requireConfirmationMessage(editionDefinition);

		if ((idForm != null) && (!idForm.isEmpty())) {
			form = this.renderLink.loadNode(idForm);
			if (form != null) {
				render = this.rendersFactory.get(form, "edit.html", this.renderLink, account);
				render.setParameter("view", formDefinition.getViewMap().get(useDefinition.getWithView().getValue()).getCode());
				renderResult = render.getOutput();
			}
		}

		map.put("label", this.language.getModelResource(formDefinition.getLabel()));
		map.put("render(node.form)", renderResult);
		map.put("idTask", this.task.getId());
		map.put("requireConfirmation", requireConfirmation);

		return block("content.state$current$action.edition", map);
	}

	private String requireConfirmationMessage(PlaceActionProperty definition) {
		PlaceActionProperty.RequireConfirmationProperty requireConfirmation = getRequireConfirmation(definition);

		if (requireConfirmation == null)
			return null;

		return requireConfirmation.getMessage() != null ? language.getModelResource(requireConfirmation.getMessage()) : block("content.state$current$action$confirmation.default", new HashMap<String, Object>());
	}

	private PlaceActionProperty.RequireConfirmationProperty getRequireConfirmation(PlaceActionProperty definition) {
		return definition.getRequireConfirmation();
	}

	private String initStateViewEnroll(PlaceProperty placeDefinition, EnrollActionProperty enrollDefinition) {
// CONTESTANTS
//		HashMap<String, Object> map = new HashMap<String, Object>();
//		String contestKey = enrollDefinition.getContest().getValue();
//		ProcessDefinition definition = (ProcessDefinition) this.definition;
//		TaskContestProperty contestDefinition = definition.getTaskContestPropertyMap().get(contestKey);
//		String activityCode = this.dictionary.getDefinitionCode(contestDefinition.getActivity().getValue());
//		TaskList taskList = this.renderLink.loadTasks(activityCode);
//		StringBuffer contestsBuffer = new StringBuffer();
//		String requireConfirmation = requireConfirmation(enrollDefinition);
//
//		for (Task task : taskList.get().values()) {
//			HashMap<String, Object> localMap = new HashMap<String, Object>();
//			localMap.put("id", task.getId());
//			localMap.put("label", task.getLabel());
//			localMap.put("createDate", task.getCreateDate());
// 			localMap.put("requireConfirmation", requireConfirmation);
//			contestsBuffer.append(block("content.state$current$action.enroll$contest", localMap));
//		}
//
//		map.put("contests", contestsBuffer);
//
//		return block("content.state$current$action.enroll", map);
		return "";
	}

	private String initStateViewWaitOnPending(PlaceProperty placeDefinition, WaitActionProperty waitDefinition) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String requireConfirmation = requireConfirmationMessage(waitDefinition);

		map.put("idTask", this.task.getId());
		map.put("code", placeDefinition.getCode());
		map.put("requireConfirmation", requireConfirmation);

		return block("content.state$current$action.wait.pending", map);
	}

	private String initStateViewWaitOnWaiting(PlaceProperty placeDefinition, WaitActionProperty waitDefinition) {
		HashMap<String, Object> map = new HashMap<>();
		Date date = new Date();
		String requireConfirmation = requireConfirmationMessage(waitDefinition);

		date.setTime(this.task.getProcess().getTimerDue(placeDefinition.getCode()));

		map.put("idTask", this.task.getId());
		map.put("code", placeDefinition.getCode());
		map.put("date", LibraryDate.getDateAndTimeString(date, Language.getCurrent(), Language.getCurrentTimeZone(), LibraryDate.Format.INTERNAL, true, "/"));
		map.put("requireConfirmation", requireConfirmation);

		return block("content.state$current$action.wait.waiting", map);
	}

	private String initStateViewDelegationOnWaiting(DelegationActionProperty delegationActionProperty, Object object) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		return block("content.state$current$action.delegation.waiting", map);
	}

	private String initStateViewSendRequestFailure(PlaceProperty placeDefinition, SendRequestActionProperty sendRequestDefinition) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("idTask", this.task.getId());
		return block("content.state$current$action.sendRequest$failure", map);
	}

	private String initStateViewSendResponseFailure(PlaceProperty placeDefinition, SendResponseActionProperty sendResponseDefinition) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("idTask", this.task.getId());
		return block("content.state$current$action.sendResponse$failure", map);
	}

	@Override
	protected String initStateView() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String history = "", currentState = "";
		String state = this.task.getState();
		ProcessBehavior process = this.task.getProcess();
		PlaceProperty placeDefinition = process.getCurrentPlace();
		String action = "";

		if (this.task.isPending()) {

			if (placeDefinition.getDelegationActionProperty() != null)
				action = this.initStateViewDelegation(placeDefinition.getDelegationActionProperty(), null);
			if (placeDefinition.getSendJobActionProperty() != null)
				action = this.initStateViewSendJob(placeDefinition.getSendJobActionProperty());
			else if (placeDefinition.getLineActionProperty() != null)
				action = this.initStateViewLine(placeDefinition, placeDefinition.getLineActionProperty());
			else if (placeDefinition.getEditionActionProperty() != null)
				action = this.initStateViewEdition(placeDefinition, placeDefinition.getEditionActionProperty());
			else if (placeDefinition.getEnrollActionProperty() != null)
				action = this.initStateViewEnroll(placeDefinition, placeDefinition.getEnrollActionProperty());
			else if (placeDefinition.getWaitActionProperty() != null)
				action = this.initStateViewWaitOnPending(placeDefinition, placeDefinition.getWaitActionProperty());

		} else if (this.task.isWaiting()) {

			if (placeDefinition.getWaitActionProperty() != null)
				action = this.initStateViewWaitOnWaiting(placeDefinition, placeDefinition.getWaitActionProperty());
			else if (placeDefinition.getDelegationActionProperty() != null)
				action = this.initStateViewDelegationOnWaiting(placeDefinition.getDelegationActionProperty(), null);

		} else if (this.task.isFailure()) {

			if (placeDefinition.getDelegationActionProperty() != null)
				action = this.initStateViewDelegationOnFailure(placeDefinition.getDelegationActionProperty());
			else if (placeDefinition.getSendRequestActionProperty() != null)
				action = this.initStateViewSendRequestFailure(placeDefinition, placeDefinition.getSendRequestActionProperty());
			else if (placeDefinition.getSendResponseActionProperty() != null)
				action = this.initStateViewSendResponseFailure(placeDefinition, placeDefinition.getSendResponseActionProperty());

		}

		map.put("idTask", this.task.getId());
		map.put("state", state);
		map.put("stateLabel", this.getTaskStateLabel(state));
		map.put("date", LibraryDate.getDateAndTimeString(this.task.getInternalUpdateDate(), Language.getCurrent(), Language.getCurrentTimeZone(), LibraryDate.Format.INTERNAL, true, "/"));

		if (action != null) {
			PlaceActionProperty placeActionDefinition = this.task.getProcess().getCurrentPlace().getPlaceActionProperty();

			if (placeActionDefinition != null)
				map.put("label", this.language.getModelResource(placeActionDefinition.getLabel()));

			map.put("action", action);

			currentState = block("content.state$current", map);
		} else
			currentState = block("content.state$current.empty", map);

		history = block("content.state$history", map);

		map.clear();
		map.put("current", currentState);
		map.put("history", history);

		return block("content.state", map);
	}

	private String initOrdersViewGroupByList() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String groupByListOptions = "", groupByList = "";
		RoleList roleList = this.renderLink.loadTaskOrdersRoleList(this.task.getId());
		int position;

		if (roleList.getCount() > 1) {
			position = 0;
			groupByListOptions = "";
			for (Role role : roleList) {
				String optionLabel = role.getLabel();

				map.put("position", String.valueOf(position));
				map.put("code", "role");
				map.put("optionCode", role.getId());
				map.put("optionLabel", optionLabel);
				position++;
				groupByListOptions += block("content.orders$groupByList$item$option", map);
				map.clear();
			}

			map.put("code", "role");
			map.put("label", block("content.orders$groupByList$item$roleLabel", map));
			map.put("options", groupByListOptions);
			groupByList += block("content.orders$groupByList$item", map);
			map.clear();
		}

		return groupByList;
	}

	@Override
	protected String initOrdersView() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("id", this.task.getId());
		map.put("orderTemplate", block("content.orders$orderTemplate:client-side", map));
		map.put("groupByList", this.initOrdersViewGroupByList());

		return block("content.orders", map);
	}

	@Override
	protected String initHistoryView() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		TaskFactPage taskHistoryPage = (TaskFactPage) this.getParameter("historyPage");
		StringBuilder factsBuilder = new StringBuilder();

		for (Fact fact : taskHistoryPage.getEntries()) {
			List<MonetLink> linkList = fact.getLinks();
			StringBuilder linksBuilder = new StringBuilder();
			String title = fact.getTitle();
			String subTitle = fact.getSubTitle();

			if (linkList != null) {
				for (MonetLink link : linkList) {
					map.put("targetId", link.getId());

					String label = link.getLabel();
					switch (link.getType()) {
						case Node:
							map.put("target", block("content.history$link.showNode", map));
							if (label == null)
								label = this.renderLink.loadNode(link.getId()).getLabel();
							break;
						case Task:
							map.put("target", block("content.history$link.showTask", map));
							if (label == null)
								label = this.renderLink.loadTask(link.getId()).getLabel();
							break;
						default:

							break;
					}
					map.put("label", label);
					linksBuilder.append(block("content.history$link", map));
				}
			}

			String user = fact.getUserId();

			map.put("createDate", LibraryDate.getDateAndTimeString(fact.getInternalCreateDate(), Language.getCurrent(), Language.getCurrentTimeZone(), LibraryDate.Format.INTERNAL, true, "/"));
			map.put("title", title != null ? title : "");
			map.put("subTitle", subTitle != null ? subTitle : "");
			map.put("user", user);
			map.put("user", user != null && !user.isEmpty() && !user.contains("|")?block("content.history$fact$user", map):"");
			map.put("links", linksBuilder.toString());

			factsBuilder.append(block("content.history$fact", map));
			map.clear();
		}

		map.put("facts", factsBuilder.toString());

		return block("content.history", map);
	}

	@Override
	protected String initConcreteView(String codeView) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String content = "";
		ProcessDefinition definition = (ProcessDefinition) this.definition;

		ProcessDefinition.ViewProperty viewDefinition = definition.getTaskViewDeclaration(codeView);

		if (viewDefinition == null) {
			map.put("codeView", codeView);
			map.put("labelDefinition", this.language.getModelResource(this.definition.getLabel()));
			return block("view.undefined", map);
		}

		ProcessDefinition.ViewProperty.ShowProperty showDefinition = viewDefinition.getShow();
		if (showDefinition.getTarget() != null)
			content = this.initNodeView("target", this.task.getTarget(), showDefinition.getTarget().getValue());
		else if (showDefinition.getShortcut() != null) {
			Node node = this.task.getShortcutInstance(showDefinition.getShortcut());
			String shortcutView = showDefinition.getShortcutView() != null?showDefinition.getShortcutView():null;
			content = this.initNodeView("shortcut", node, shortcutView);
		}

		return content;
	}

}
