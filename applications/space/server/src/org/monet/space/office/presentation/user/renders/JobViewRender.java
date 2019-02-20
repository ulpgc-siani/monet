package org.monet.space.office.presentation.user.renders;

import org.monet.space.kernel.model.User;

import java.util.HashMap;

public class JobViewRender extends TaskViewRender {

	public JobViewRender() {
		super();
	}

	@Override
	protected String initStateView() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		User owner = this.task.getOwner();
		String blockName = null;

		if (owner == null) {
			blockName = "content.state$pending";
			if (this.task.isAborted()) blockName = "content.state$aborted";
			else if (this.task.isFinished()) blockName = "content.state$finished";
		} else {
			blockName = "content.state$pending.owner";
			if (this.task.isAborted()) blockName = "content.state$aborted.owner";
			else if (this.task.isFinished()) blockName = "content.state$finished.owner";
			map.put("fullname", owner.getInfo().getFullname());
		}

		return block(blockName, map);
	}

	@Override
	protected String initOrdersView() {
		return "";
	}

	@Override
	protected String initHistoryView() {
		return "";
	}

	@Override
	protected String initConcreteView(String codeView) {
		return "";
	}

	@Override
	protected void init() {
		loadCanvas("view.task.job");
		super.init();
	}

}
