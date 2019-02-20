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

package org.monet.space.office.control.actions;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.monet.metamodel.*;
import org.monet.metamodel.Distribution.ShowProperty;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.components.ComponentDatawareHouse;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.DashboardLayer;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.components.layers.RoleLayer;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.office.core.constants.ErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ActionLoadAccount extends Action {
	private NodeLayer nodeLayer;
	private DashboardLayer dashboardLayer;
	private RoleLayer roleLayer;

	public ActionLoadAccount() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		this.dashboardLayer = ComponentDatawareHouse.getInstance().getDashboardLayer();
		this.roleLayer = ComponentFederation.getInstance().getRoleLayer();
	}

	public String execute() {
		Account account = this.getAccount();
		JSONObject jsonAccount;

		if (!this.getFederationLayer().isLogged())
			return ErrorCode.USER_NOT_LOGGED;

		Language.fillCurrentTimeZone(request);
		this.loadProfile(account);

		NotificationList notificationList = ComponentPersistence.getInstance()
			.getNotificationLayer()
			.loadNotificationList(account.getUser().getId(), 0, 6);

		Task currentInitializerTask = ComponentPersistence.getInstance().getTaskLayer().getCurrentInitializerTask();
		Task task = null;
		if (currentInitializerTask != null && account.canResolveInitializerTask(currentInitializerTask))
			task = currentInitializerTask;

		AgentLogger.getInstance().info("Loading account %s", account.getId());

		jsonAccount = account.toJson(Profile.OFFICE);
		jsonAccount.put("initializertask", (task != null) ? task.toJson() : null);
		jsonAccount.put("notifications", notificationList.toJson());
        jsonAccount.put("pendingTasks", this.fillPendingTasks());
		jsonAccount.put("links", this.getLinks(account));
		jsonAccount.put("instanceId", UUID.randomUUID().toString());

		AgentLogger.getInstance().info("Account %s loaded", account.getId());

		return jsonAccount.toJSONString();
	}

    private JSONObject fillPendingTasks() {
        TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
        Account account = this.getAccount();
        JSONObject pendingTasks = new JSONObject();

        TaskSearchRequest searchRequest = new TaskSearchRequest();

	    searchRequest.addParameter(Task.Parameter.SITUATION, Task.Situation.ACTIVE);
        searchRequest.addParameter(Task.Parameter.INBOX, Task.Inbox.TASKBOARD);
        pendingTasks.put("taskboard", taskLayer.searchTasksCount(account, searchRequest));

	    searchRequest.addParameter(Task.Parameter.SITUATION, Task.Situation.ALIVE);
        searchRequest.addParameter(Task.Parameter.INBOX, Task.Inbox.TASKTRAY);
        pendingTasks.put("tasktray", taskLayer.searchTasksCount(account, searchRequest));

        return pendingTasks;
    }

	private Node getEnvironmentNode(Account account, String key) {
		Dictionary dictionary = Dictionary.getInstance();
		NodeDefinition definition = dictionary.getNodeDefinition(key);
		String code = definition.getCode();
		Node result = null;

		if (definition.isDesktop())
			result = this.nodeLayer.locateNode(code);
		else if (definition.isEnvironment()) {
			ArrayList<Node> environmentNodes = account.getEnvironmentNodes();
			for (Node environmentNode : environmentNodes) {
				if (environmentNode.getDefinition().getCode().equals(code))
					return environmentNode;
			}
		}

		return result;
	}

	private JSONArray getLinks(Account account) {
		JSONArray result = new JSONArray();
		Distribution distribution = BusinessUnit.getInstance().getDistribution();
		Dictionary dictionary = Dictionary.getInstance();
		ShowProperty showDefinition = distribution.getShow();

		if (showDefinition == null)
			return result;

		for (Ref definitionRef : showDefinition.getTabEnvironment()) {
			Node node = this.getEnvironmentNode(account, definitionRef.getValue());

			if (node == null)
				continue;

			NodeDefinition definition = node.getDefinition();
			boolean visible = false;

			if (!definition.isEnvironment())
				continue;

			if (definition.isDesktop()) {
				DesktopDefinition desktopDefinition = (DesktopDefinition) definition;
				visible = (desktopDefinition.getFor() != null) ? this.isLinkVisible(dictionary, desktopDefinition.getFor().getRole()) : true;
			} else if (definition.isContainer() && definition.isEnvironment()) {
				ContainerDefinition containerDefinition = (ContainerDefinition) definition;
				visible = (containerDefinition.getFor() != null) ? this.isLinkVisible(dictionary, containerDefinition.getFor().getRole()) : true;
			}

			if (visible)
				result.add(this.createLink(node.getId(), "desktop", node.getLabel(), null));
		}

		for (Ref dashboardRef : showDefinition.getTabDashboard()) {
			Dashboard dashboard = this.dashboardLayer.load(dictionary.getDefinitionCode(dashboardRef.getDefinition()));
			DashboardDefinition definition = dashboard.getDefinition();
			boolean visible = (definition.getFor() != null) ? this.isLinkVisible(dictionary, definition.getFor().getRole()) : true;
			String viewCode = definition.getView(dashboardRef.getValue()).getCode();

			if (visible)
				result.add(this.createLink(dashboard.getCode(), "dashboard", dashboard.getLabel(), viewCode));
		}

		if (showDefinition.getTabTaskboard() != null) {
			boolean visible = (showDefinition.getTabTaskboard().getFor().size() > 0) ? this.isLinkVisible(dictionary, showDefinition.getTabTaskboard().getFor()) : true;
			if (visible) {
				ShowProperty.TabTaskboardProperty.ViewEnumeration view = showDefinition.getTabTaskboard().getView();
				result.add(this.createLink("taskboard", "taskboard", null, view!=null?view.toString().toLowerCase():null));
			}
		}

		if (showDefinition.getTabTasktray() != null) {
			boolean visible = (showDefinition.getTabTasktray().getFor().size() > 0) ? this.isLinkVisible(dictionary, showDefinition.getTabTasktray().getFor()) : true;
			if (visible) {
				ShowProperty.TabTasktrayProperty.ViewEnumeration view = showDefinition.getTabTasktray().getView();
				result.add(this.createLink("tasktray", "tasktray", null, view!=null?view.toString().toLowerCase():null));
			}
		}

		if (showDefinition.getTabNews() != null){
			boolean visible = (showDefinition.getTabNews().getFor().size() > 0) ? this.isLinkVisible(dictionary, showDefinition.getTabNews().getFor()) : true;
			if (visible)
				result.add(this.createLink("news", "news", null, null));
		}

		if (showDefinition.getTabRoles() != null) {
			boolean visible = (showDefinition.getTabRoles().getFor().size() > 0) ? this.isLinkVisible(dictionary, showDefinition.getTabRoles().getFor()) : true;
			if (visible)
				result.add(this.createLink("roles", "roles", null, null));
		}

		if (showDefinition.getTabTrash() != null) {
			boolean visible = (showDefinition.getTabTrash().getFor().size() > 0) ? this.isLinkVisible(dictionary, showDefinition.getTabTrash().getFor()) : true;
			if (visible)
				result.add(this.createLink("trash", "trash", null, null));
		}

		return result;
	}

	private boolean isLinkVisible(Dictionary dictionary, List<Ref> roleRefs) {
		boolean visible = false;

		for (Ref role : roleRefs) {
			String roleCode = dictionary.getDefinitionCode(role.getValue());
			if (this.roleLayer.existsNonExpiredUserRole(roleCode, this.getAccount().getUser())) {
				visible = true;
				break;
			}
		}

		return visible;
	}

	private JSONObject createLink(String id, String type, String label, String view) {
		JSONObject jsonLink = new JSONObject();
		jsonLink.put("id", id);
		jsonLink.put("type", type);
		jsonLink.put("label", label);
		jsonLink.put("view", view);
		return jsonLink;
	}

}