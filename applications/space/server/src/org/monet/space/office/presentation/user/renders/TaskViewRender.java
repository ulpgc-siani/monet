package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.TaskDefinition;
import org.monet.space.office.core.model.Language;
import org.monet.space.kernel.model.Task;

import java.util.HashMap;

public abstract class TaskViewRender extends ViewRender {
	protected Task task;
	protected TaskDefinition definition;
	protected Language language = Language.getInstance();

	public TaskViewRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
		this.task = (Task) target;
		this.definition = this.task.getDefinition();
	}

	@Override
	protected String initView(String codeView) {
		HashMap<String, Object> map = new HashMap<>();
		String content = "";

		if (codeView == null) {
			map.put("codeView", codeView);
			map.put("labelDefinition", this.language.getModelResource(this.definition.getLabel()));
			return block("view.undefined", map);
		}

		if (codeView.equals("state"))
			content = this.initStateView();
		else if (codeView.equals("orders"))
			content = this.initOrdersView();
		else if (codeView.equals("history"))
			content = this.initHistoryView();
		else
			content = this.initConcreteView(codeView);

		map.put("idTask", this.task.getId());
		map.put("codeView", codeView);
		map.put("content", content);

		return block("view", map);
	}

	protected abstract String initStateView();

	protected abstract String initOrdersView();

	protected abstract String initHistoryView();

	protected abstract String initConcreteView(String codeView);

	@Override
	protected void init() {
		loadCanvas("view.task");
		super.init();
	}

}
