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

package org.monet.space.kernel.model;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.monet.metamodel.DocumentDefinition;
import org.monet.metamodel.DocumentDefinitionBase.SignaturesProperty.SignatureProperty;
import org.monet.metamodel.RoleDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.model.wrappers.NodeDocumentWrapper;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.*;

public class Account extends BaseObject {
	protected String codeBusinessUnit;
	protected HashMap<String, ArrayList<Node>> environmentNodes;
	protected HashMap<String, ArrayList<Dashboard>> dashboards;
	protected String currentRole;
	protected User user;
	protected Map<String, Profile> profiles;
	protected RoleList roleList;

	public static final String ROOTNODE = "RootNode";
	public static final String TEAMLIST = "TeamList";
	public static final String ROLELIST = "RoleList";

	public Account() {
		super();
		this.codeBusinessUnit = Strings.EMPTY;
		this.environmentNodes = new HashMap<>();
		this.dashboards = new HashMap<>();
		this.currentRole = "";
		this.user = new User();
		this.profiles = new HashMap<>();
		this.roleList = new RoleList();
	}

	public String getCodeBusinessUnit() {
		return this.codeBusinessUnit;
	}

	public void setCodeBusinessUnit(String codeBusinessUnit) {
		this.codeBusinessUnit = codeBusinessUnit;
	}

	public String getCurrentRole() {
		return this.currentRole;
	}

	public void setCurrentRole(String role) {
		this.currentRole = role;
	}

	public HashMap<String, Task> getInitializerTasks() {
		return new HashMap<>();
	}

	public HashMap<String, ArrayList<Node>> getEnvironmentNodesByRole() {
		return this.environmentNodes;
	}

	public ArrayList<Node> getEnvironmentNodes() {
		ArrayList<Node> result = new ArrayList<>();

		for (List<Node> nodes : this.environmentNodes.values())
			for (Node node : nodes)
				result.add(node);

		return result;
	}

	public void setEnvironmentNodesByRole(HashMap<String, ArrayList<Node>> environmentNodes) {
		this.environmentNodes = environmentNodes;
	}

	public HashMap<String, ArrayList<Dashboard>> getDashboardsByRole() {
		return this.dashboards;
	}

	public ArrayList<Dashboard> getDashboards() {
		ArrayList<Dashboard> result = new ArrayList<>();

		for (List<Dashboard> dashboards : this.dashboards.values())
			for (Dashboard dashboard : dashboards)
				result.add(dashboard);

		return result;
	}

	public void setDashboardsByRole(HashMap<String, ArrayList<Dashboard>> dashboards) {
		this.dashboards = dashboards;
	}

	public Task getInitializerTask() {
		return this.getInitializerTasks().get(this.currentRole);
	}

	public Node getRootNode() {
		LinkedHashMap<String, String> preferencesMap = this.getUser().getInfo().getPreferences();
		String rootNodeId = preferencesMap.get("rootNode");
		HashMap<String, ArrayList<Node>> environmentNodes = this.getEnvironmentNodesByRole();

		for (ArrayList<Node> nodeList : environmentNodes.values()) {
			for (Node node : nodeList)
				if (node.getId().equals(rootNodeId))
					return node;
		}

		if (!environmentNodes.containsKey(this.currentRole))
			return environmentNodes.values().size() > 0 ? environmentNodes.values().iterator().next().get(0) : null;

		return environmentNodes.get(this.currentRole).get(0);
	}

	public Dashboard getRootDashboard() {
		LinkedHashMap<String, String> preferencesMap = this.getUser().getInfo().getPreferences();
		String rootDashboardId = preferencesMap.get("rootDashboard");
		HashMap<String, ArrayList<Dashboard>> dashboards = this.getDashboardsByRole();

		for (ArrayList<Dashboard> dashboardList : dashboards.values()) {
			for (Dashboard dashboard : dashboardList)
				if (dashboard.getId().equals(rootDashboardId))
					return dashboard;
		}

		if (!dashboards.containsKey(this.currentRole))
			return dashboards.values().size() > 0 ? dashboards.values().iterator().next().get(0) : null;

		return dashboards.get(this.currentRole).get(0);
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Map<String, Profile> getProfiles() {
		return this.profiles;
	}

	public <T extends Profile> T getProfile(String code) {
		if (!this.profiles.containsKey(code))
			return null;
		return (T)this.profiles.get(code);
	}

	public void setProfile(String code, Profile profile) {
		this.profiles.put(code, profile);
	}

	public RoleList getRoleList() {
		this.removeLoadedAttribute(Account.ROLELIST);
		onLoad(this, Account.ROLELIST);
		return this.roleList;
	}

	public void setRoleList(RoleList roleList) {
		this.roleList = roleList;
		this.addLoadedAttribute(Account.ROLELIST);
	}

	public boolean hasPermissions() {
		Map<String, ArrayList<Node>> environments = this.getEnvironmentNodesByRole();
		Map<String, ArrayList<Dashboard>> dashboards = this.getDashboardsByRole();

        return !(environments.size() <= 0 && dashboards.size() <= 0);

    }

	public JSONObject toJson() {
		JSONObject result = new JSONObject();
		boolean partialLoading = this.isPartialLoading();

		result.put("id", this.getId());
		result.put("user", this.getUser().toJson());

		if (partialLoading)
			this.disablePartialLoading();

		Task task = this.getInitializerTask();
		Node rootNode = this.getRootNode();
		Dashboard rootDashboard = this.getRootDashboard();

		result.put("initializertask", task != null ? task.toJson() : null);
		result.put("rootnode", rootNode != null ? rootNode.toJson() : "");
		result.put("rootdashboard", rootDashboard != null ? rootDashboard.toJson() : "");
		result.put("roles", this.getEnvironmentNodesByRole().keySet());
		if (partialLoading)
			this.enablePartialLoading();

		return result;
	}

	public JSONObject toJson(String profileName) {
		Profile profile;
		JSONObject result = new JSONObject();
		boolean partialLoading = this.isPartialLoading();

		result.put("id", this.getId());
		result.put("user", this.getUser().toJson());

		if (partialLoading)
			this.disablePartialLoading();

		HashMap<String, ArrayList<Node>> environmentRoles = this.getEnvironmentNodesByRole();
		Node rootNode = this.getRootNode();
		Dashboard rootDashboard = this.getRootDashboard();

		result.put("rootnode", rootNode != null ? rootNode.toJson() : "");
		result.put("rootdashboard", rootDashboard != null ? rootDashboard.toJson() : "");
		result.put("roles", environmentRoles.keySet());

		this.environmentsToJson(result, environmentRoles, rootNode);
		this.dashboardsToJson(result, dashboards, rootDashboard);

		if (partialLoading)
			this.enablePartialLoading();

		if (!this.profiles.containsKey(profileName))
			return result;
		profile = this.profiles.get(profileName);
		result.put("profile", profile.toJson());

		return result;
	}

	private void environmentsToJson(JSONObject result, HashMap<String, ArrayList<Node>> environmentRoles, Node rootNode) {
		JSONArray environments = new JSONArray();
		HashSet<String> parsedEnvironments = new HashSet<>();

		for (ArrayList<Node> nodeList : environmentRoles.values()) {
			for (Node node : nodeList) {
				String id = node.getId();

				if (parsedEnvironments.contains(id))
					continue;

				JSONObject environment = new JSONObject();
				environment.put("id", id);
				environment.put("label", node.getLabel());
				environment.put("active", rootNode != null && id.equals(rootNode.getId()));
				environment.put("disabled", node.getDefinition().isDisabled());
				environments.add(environment);

				parsedEnvironments.add(id);
			}
		}

		result.put("environments", environments);
	}

	private void dashboardsToJson(JSONObject result, HashMap<String, ArrayList<Dashboard>> dashboardRoles, Dashboard rootDashboard) {
		JSONArray dashboards = new JSONArray();
		HashSet<String> parsedDashboards = new HashSet<>();

		for (ArrayList<Dashboard> dashboardList : dashboardRoles.values()) {
			for (Dashboard dashboard : dashboardList) {
				String id = dashboard.getId();

				if (parsedDashboards.contains(id))
					continue;

				JSONObject serialized = new JSONObject();
				serialized.put("id", id);
				serialized.put("label", dashboard.getLabel());
				serialized.put("active", rootDashboard != null && id.equals(rootDashboard.getId()));
				serialized.put("disabled", dashboard.getDefinition().isDisabled());
				dashboards.add(serialized);

				parsedDashboards.add(id);
			}
		}

		result.put("dashboards", dashboards);
	}

	public void fromJson(String data) throws ParseException {
		JSONObject jsonData = (JSONObject) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(data);
		this.user.fromJson(jsonData.get("user").toString());
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {

	}

	public boolean canSign(Node node) {
		if (!node.isDocument())
			return false;

		DocumentDefinition definition = (DocumentDefinition) node.getDefinition();

		if (definition.getSignatures() == null)
			return false;

		NodeDocumentWrapper wrapper = new NodeDocumentWrapper(node);
		Map<String, SignatureProperty> signaturesDefinition = definition.getSignatures().getSignatureMap();
		RoleList roleList = this.getRoleList();
		Dictionary dictionary = Dictionary.getInstance();

		for (SignatureProperty signatureDefinition : signaturesDefinition.values()) {
			int countSignatures = wrapper.getCountSignaturesOfType(signatureDefinition);

			for (int position = 0; position < countSignatures; position++) {
				if (wrapper.isSignatureMade(signatureDefinition.getCode(), position))
					continue;

				for (Ref forDefinition : signatureDefinition.getFor()) {
					if (roleList.exist(dictionary.getDefinitionCode(forDefinition.getValue())))
						return true;
				}
			}
		}

		return false;
	}

	public boolean canResolveInitializerTask(Task currentInitializerTask) {
		Ref roleRef = currentInitializerTask.getDefinition().getRole();

		if (roleRef == null)
			return true;

		RoleDefinition roleDefinition = Dictionary.getInstance().getRoleDefinition(roleRef.getValue());
		RoleList roleList = this.getRoleList();

		for (Role role : roleList)
			if (role.getCode().equals(roleDefinition.getCode()))
				return true;

		return false;
	}

}
