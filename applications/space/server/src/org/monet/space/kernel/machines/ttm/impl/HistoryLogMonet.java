package org.monet.space.kernel.machines.ttm.impl;

import com.google.inject.Inject;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.machines.ttm.persistence.HistoryLog;
import org.monet.space.kernel.model.Fact;
import org.monet.space.kernel.model.TaskFact;

import java.util.Date;

public class HistoryLogMonet implements HistoryLog {

	@Inject
	private TaskLayer taskLayer;

	private String taskId;

	@Override
	public void init(String id) {
		this.taskId = id;
	}

	@Override
	public void add(String data) {
		TaskFact fact = new TaskFact();
		fact.setTaskId(this.taskId);
		fact.setTitle(data);
		fact.setCreateDate(new Date());
		this.taskLayer.addTaskFact(fact);
	}

	@Override
	public void add(Fact fact) {
		fact.setTaskId(this.taskId);
		this.taskLayer.addTaskFact(fact);
	}

}
