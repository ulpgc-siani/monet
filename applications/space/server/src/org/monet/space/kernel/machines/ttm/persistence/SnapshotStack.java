package org.monet.space.kernel.machines.ttm.persistence;

import org.monet.space.kernel.machines.ttm.model.Snapshot;

public interface SnapshotStack {

	void init(String id);

	Snapshot pick();

	Snapshot pop();

	void push(Snapshot snapshot);

	boolean isEmpty();

}
