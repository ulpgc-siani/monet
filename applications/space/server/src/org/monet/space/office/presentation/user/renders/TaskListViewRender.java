package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.internal.Ref;
import org.monet.space.office.configuration.Configuration;
import org.monet.space.kernel.library.LibraryString;
import org.monet.space.kernel.model.*;

import java.util.HashMap;
import java.util.List;

public class TaskListViewRender extends OfficeRender {

	public TaskListViewRender() {
		super();
	}

	private String initTasktrayTemplate() {
		Configuration configuration = Configuration.getInstance();

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("themeSource", configuration.getApiUrl() + "?op=loadthemefile");
		map.put("chartSource", configuration.getApiUrl() + "?op=rendertasktimeline");
		return block("view$tasktray$template:client-side", map);
	}

	private String initTaskboardTemplate() {
		Configuration configuration = Configuration.getInstance();

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("themeSource", configuration.getApiUrl() + "?op=loadthemefile");
		map.put("chartSource", configuration.getApiUrl() + "?op=rendertasktimeline");
		return block("view$taskboard$template:client-side", map);
	}

	private void initCustomView(HashMap<String, Object> contentMap) {
		Configuration configuration = Configuration.getInstance();
		StringBuilder tasks = new StringBuilder();
		TaskList taskList = (TaskList) this.target;
		HashMap<String, Object> map = new HashMap<String, Object>();

		for (Task task : taskList) {
			int newMessagesCount = task.getNewMessagesCount();
			String comments = task.getComments();

			map.put("id", task.getId());
			map.put("state", task.getState());
			map.put("label", task.getLabel());
			map.put("themeSource", configuration.getApiUrl() + "?op=loadthemefile");
			map.put("chartSource", configuration.getApiUrl() + "?op=rendertasktimeline");
			map.put("type", task.getDefinition().getType().toString());
			map.put("definitionLabel", task.getDefinition().getLabel());
			map.put("description", task.getDescription());
			map.put("startDate", task.getStartDate());
			map.put("newMessagesClass", newMessagesCount > 0 ? "active" : "");
			map.put("newMessagesCount", newMessagesCount);
			map.put("urgentStyle", task.isUrgent() ? "active" : "");
			map.put("hasComments", comments != null && !comments.isEmpty());

			tasks.append(block("view.custom$item", map));
		}

		contentMap.put("tasks", tasks.toString());
	}

	private String initGroupByListRoleItem(String view, RoleList roleList) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String groupByListOptions = "";
		int position;

		position = 0;
		groupByListOptions = "";

		if (view.equals("tasktray")) return "";
		if (roleList.getCount() <= 1) return "";

		for (Role role : roleList.get().values()) {
			map.put("position", String.valueOf(position));
			map.put("code", "role");
			map.put("optionCode", role.getCode());
			map.put("optionLabel", role.getLabel());
			position++;
			groupByListOptions += block("view$groupByList$item$option", map);
		}

		map.put("code", "role");
		map.put("label", block("view$groupByList$item$roleLabel", map));
		map.put("options", groupByListOptions);

		return block("view$groupByList$item", map);
	}

	private String initGroupByListTypeItem(String view, List<TaskType> taskTypes) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String groupByListOptions = "";
		int position;

		position = 0;
		groupByListOptions = "";

		if (taskTypes.size() <= 1) return "";

		for (TaskType taskType : taskTypes) {
			map.put("position", String.valueOf(position));
			map.put("code", "type");
			map.put("optionCode", taskType.getCode().replaceAll("(\\n|\\t)", " "));
			map.put("optionLabel", LibraryString.cleanSpecialChars(taskType.getLabel().replaceAll("\\n", " ")));
			position++;
			groupByListOptions += block("view$groupByList$item$option", map);
		}

		map.put("code", "type");
		map.put("label", block("view$groupByList$item$typeLabel", map));
		map.put("options", groupByListOptions);

		return block("view$groupByList$item", map);
	}

	private String initGroupByListOwnerItem(String view, UserList userList) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String groupByListOptions = "";
		int position;

		position = 0;
		groupByListOptions = "";

		if (view.equals("tasktray")) return "";
		if (userList.getCount() <= 1) return "";

		map.put("position", String.valueOf(position));
		map.put("code", "owner");
		map.put("optionCode", "-1");
		map.put("optionLabel", block("view$groupByList$item$unassignedLabel", map));
		position++;
		groupByListOptions += block("view$groupByList$item$option", map);

		for (User user : userList.get().values()) {
			map.put("position", String.valueOf(position));
			map.put("code", "owner");
			map.put("optionCode", user.getId());
			map.put("optionLabel", user.getInfo().getFullname());
			position++;
			groupByListOptions += block("view$groupByList$item$option", map);
		}

		map.put("code", "owner");
		map.put("label", block("view$groupByList$item$ownerLabel", map));
		map.put("options", groupByListOptions);

		return block("view$groupByList$item", map);
	}

	private String initGroupByListSenderItem(String view, UserList userList) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String groupByListOptions = "";
		int position;

		position = 0;
		groupByListOptions = "";

		if (userList.getCount() <= 1) return "";

		for (User user : userList.get().values()) {
			map.put("position", String.valueOf(position));
			map.put("code", "owner");
			map.put("optionCode", user.getId());
			map.put("optionLabel", user.getInfo().getFullname());
			position++;
			groupByListOptions += block("view$groupByList$item$option", map);
		}

		map.put("code", "owner");
		map.put("label", block("view$groupByList$item$senderLabel", map));
		map.put("options", groupByListOptions);

		return block("view$groupByList$item", map);
	}

	@Override
	protected void init() {
		String view = this.getParameterAsString("view");
		HashMap<String, Object> map = new HashMap<String, Object>();
		TaskFilters filters = this.renderLink.loadTaskFilters(org.monet.space.office.core.model.Language.getCurrent());
		RoleList roleList = this.renderLink.loadTasksRoleList();
		UserList ownerList = this.renderLink.loadTasksOwnerList();
		UserList senderList = null;
		String blockName = "view";
		String folder = this.getParameterAsString("folder");

		if (folder.isEmpty())
			folder = Task.Situation.ACTIVE;

		loadCanvas("view.tasklist");

		map.put("id", this.getParameterAsString("id"));
		map.put("defaultFolder", folder);
		map.put("view", view);

		if (view.equals("tasktray")) {
			String idUser = this.account.getUser().getId();
			map.put("taskTemplate", this.initTasktrayTemplate());
			senderList = this.renderLink.loadTasksSenderList(idUser);
		} else if (view.equals("taskboard")) {
			map.put("taskTemplate", this.initTaskboardTemplate());
			senderList = this.renderLink.loadTasksSenderList();
			this.initCustomView(map);
		} else if (view.equals("custom")) {
			senderList = this.renderLink.loadTasksSenderList();
			this.initCustomView(map);
		}

		List<Ref> roleRefs = this.account.getRootNode().getDefinition().getRoles();
		map.put("desktopRole", (roleRefs != null && roleRefs.size() > 0) ? this.dictionary.getDefinitionCode(roleRefs.get(0).getValue()) : "");
		map.put("groupByListSelection", block("view$" + view + "$groupByListSelection", map));

		map.put("from", view);
		map.put("groupByListRoles", this.initGroupByListRoleItem(view, roleList));
		map.put("groupByListTypes", this.initGroupByListTypeItem(view, filters.types));
		map.put("groupByListOwners", this.initGroupByListOwnerItem(view, ownerList));
		map.put("groupByListSenders", this.initGroupByListSenderItem(view, senderList));

		if (existsBlock("view." + view))
			blockName = "view." + view;

		addMark("inbox", view);
		addMark("view", block(blockName, map));
	}

}