package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.ProcessDefinition;
import org.monet.metamodel.ProcessDefinitionBase;
import org.monet.metamodel.ProcessDefinitionBase.ViewProperty.ShowProperty;
import org.monet.metamodel.TaskDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.model.User;
import org.monet.space.mobile.model.Language;
import org.monet.space.office.configuration.Configuration;

import java.util.HashMap;

public class TaskPageRender extends OfficeRender {
	protected Task task;
	protected TaskDefinition definition;

	public TaskPageRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
		this.task = (Task) target;
		this.definition = this.task.getDefinition();
	}

	@Override
	protected void init() {
		loadCanvas("page.task");

		addMark("id", this.task.getId());
		addMark("finished", this.task.isFinished() ? "finished" : "");
		addMark("aborted", this.task.isAborted() ? "aborted" : "");
		addMark("state", this.task.getState());

		this.initControlInfo();
		this.initConfiguration();
		this.initHeader();
		this.initTabs();
	}

	protected void initControlInfo() {
		addMark("code", this.task.getCode());
		addMark("from", this.getParameterAsString("from"));
	}

	protected void initConfiguration() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String operations = "";

		map.put("idTask", this.task.getId());
		operations += block("operation", map);

		if (!this.task.isFinished()) {
			operations += block("operation.refresh", map);
			operations += block("operation.alert", map);
			operations += block("operation$abort", map);
		}

		if (this.task.getOwner() == null)
			operations += block("operation.assign", map);

		addMark("operations", operations);
	}

	protected void initHeader() {
		Configuration configuration = Configuration.getInstance();
		HashMap<String, Object> map = new HashMap<String, Object>();
		User loggedUser = this.account.getUser();
		User owner = this.task.getOwner();
		User sender = this.task.getSender();
		String type = this.definition.getType().toString();
		String id = this.task.getId();
		String blockName = "";
		String from = calculateFrom();

		map.put("id", id);
		map.put("idTask", id);
		map.put("label", this.task.getLabel());
		map.put("definitionLabel", this.task.getDefinition().getLabelString());
		map.put("description", this.task.getDescription());
		map.put("createDate", this.task.getCreateDate());
		map.put("updateDate", this.task.getUpdateDate());
		map.put("startDate", this.task.getStartDate());
		map.put("themeSource", configuration.getApiUrl() + "?op=loadthemefile");
		map.put("chartSource", configuration.getApiUrl() + "?op=rendertasktimeline");
		map.put("type", type);
		map.put("typeLabel", block("type." + type, map));
		map.put("urgentStyle", this.task.isUrgent() ? "true" : "");

		map.put("owner", "");
		map.put("sender", "");
		if (owner != null) {
			if (owner.getId().equals(loggedUser.getId())) {
				if (sender != null) {
					map.put("senderFullname", sender.getInfo().getFullname());
					map.put("sender", block("header$sender", map));
				} else
					map.put("sender", block("header$sender.empty", map));
			} else {
				map.put("ownerFullname", owner.getInfo().getFullname());
				map.put("owner", block("header$owner", map));
			}
		}

		if (this.task.isInitializer())
			blockName = "header$breadcrumbs.initializer";
		else
			blockName = "header$breadcrumbs." + from;

		map.put("breadcrumbs", block(blockName, map));
		map.put("random", Math.random());

		this.initOperations(map);

		addMark("header", block("header", map));
	}

	private String calculateFrom() {
		String from = this.getParameterAsString("from");

		if (!from.isEmpty())
			return from;

		String ownerId = task.getOwnerId();
		if (ownerId == null)
			return "taskboard";

		return this.account.getUser().getId().equals(ownerId) ? "tasktray" : "taskboard";
	}

	protected void initOperations(HashMap<String, Object> headerMap) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String operations = "";
		String moreOperations = "";

		map.put("idTask", this.task.getId());
		operations += block("operation", map);

		if (!this.task.isFinished()) {
			operations += block("operation.refresh", map);
			operations += block("operation.alert", map);
			moreOperations += block("operation.more$abort", map);
			//moreOperations += block("operation.more$priorize", map);
			//moreOperations += block("operation.more$suggest", map);
			//moreOperations += block("operation.more$setTaskGoal", map);
		}

		if (this.task.getOwner() == null)
			operations += block("operation.assign", map);

		if (!moreOperations.isEmpty()) {
			map.put("moreOperations", moreOperations);
			operations += block("operation.more", map);
		}

		headerMap.put("operations", operations);
	}

	protected String initTab(String label, String codeTab) {
		HashMap<String, Object> map = new HashMap<>();

		if (label.isEmpty()) label = block("tab." + codeTab + "$label", map);

		map.put("code", codeTab);
		map.put("label", label);

		OfficeRender render = this.rendersFactory.get(this.task, this.template, this.renderLink, account);
		render.setParameters(this.getParameters());
		render.setParameter("view", codeTab);
		map.put("render(view.task)", render.getOutput());

		return block("tab", map);
	}

	protected void initTabs() {
		HashMap<String, Object> map = new HashMap<>();
		String tabsList = "";
		Language language = Language.getInstance();
		int count = this.renderLink.requestTaskOrderListItemsCount(this.task.getId());
		String defaultTabCode = block("tab.default", map);

		tabsList += this.initTab("", "state");

		if (!this.definition.isJob()) {
			ProcessDefinition definition = ((ProcessDefinition) this.definition);

			for (ProcessDefinition.ViewProperty viewDefinition : definition.getViewList()) {
				ShowProperty showDefinition = viewDefinition.getShow();
				String label = language.getModelResource(viewDefinition.getLabel());

				if (showDefinition == null) continue;

				if (!hasViewPermissions(viewDefinition))
					continue;

				if (showDefinition.getShortcut() != null) {
					Node node = this.task.getShortcutInstance(showDefinition.getShortcut());
					if (node == null) continue;
					if (viewDefinition.isDefault()) defaultTabCode = viewDefinition.getCode();
					tabsList += this.initTab(label, viewDefinition.getCode());
				} else if (showDefinition.getTarget() != null && this.task.getTarget() != null) {
					if (viewDefinition.isDefault()) defaultTabCode = viewDefinition.getCode();
					tabsList += this.initTab(label, viewDefinition.getCode());
				} else if (showDefinition.getOrders() != null && count > 0) {
					tabsList += this.initTab(label, "orders");
				}
			}
		}

		map.put("defaultTab", defaultTabCode);
		map.put("tabsList", tabsList);

		addMark("tabs", block("tabs", map));
	}

	private boolean hasViewPermissions(ProcessDefinition.ViewProperty viewDefinition) {
		ProcessDefinitionBase.ViewProperty.ForProperty forDefinition = viewDefinition.getFor();

		if (forDefinition == null)
			return true;

		for (Ref roleRef : forDefinition.getRole()) {
			String roleCode = dictionary.getDefinitionCode(roleRef.getValue());

			if (account.getRoleList().contains(roleCode))
				return true;
		}

		return false;
	}

}
