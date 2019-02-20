package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.TaskDefinition;
import org.monet.space.kernel.model.TaskList;

import java.util.HashMap;

public class TaskListPageRender extends OfficeRender {

	public TaskListPageRender() {
		super();
	}

	private String initAddList() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String addList = "";

		for (TaskDefinition definition : this.dictionary.getTaskDefinitionList()) {
			map.put("code", definition.getCode());
			map.put("label", definition.getLabelString());
			map.put("description", definition.getDescription());
			addList += block("addList$item", map);
			map.clear();
		}

		return addList;
	}

	@Override
	protected void init() {
		loadCanvas("page.tasklist");

		addMark("addList", this.initAddList());

		String view = this.getParameterAsString("view");
		addMark("codeView", view);

		String labelBlock = "label";
		if (existsBlock("label." + view)) labelBlock = "label." + view;
		addMark("label", block(labelBlock, new HashMap<String, Object>()));

		OfficeRender taskListRender = this.rendersFactory.get(new TaskList(), "preview.html?mode=view&view=", this.renderLink, account);
		taskListRender.setParameters(this.getParameters());
		taskListRender.setParameter("view", view);
		addMark("render(view.tasklist)", taskListRender.getOutput());
	}

}