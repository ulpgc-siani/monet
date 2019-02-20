package org.monet.space.kernel.machines.ttm.impl;

import com.google.inject.Inject;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.machines.ttm.model.Snapshot;
import org.monet.space.kernel.machines.ttm.persistence.SnapshotStack;

public class SnapshotStackMonet implements SnapshotStack {

	@Inject
	private TaskLayer taskLayer;

	private String taskId;

	@Override
	public void init(String id) {
		this.taskId = id;
	}

	@Override
	public Snapshot pick() {
		return this.taskLayer.loadLastSnapshot(this.taskId);
	}

	@Override
	public Snapshot pop() {
		return this.taskLayer.loadAndDeleteLastSnapshot(this.taskId);
	}

	@Override
	public void push(Snapshot snapshot) {
		this.taskLayer.saveSnapshot(this.taskId, snapshot);
	}

	@Override
	public boolean isEmpty() {
		return this.taskLayer.hasSnapshots(this.taskId);
	}

}
