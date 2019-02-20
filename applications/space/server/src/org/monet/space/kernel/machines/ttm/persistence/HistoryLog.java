package org.monet.space.kernel.machines.ttm.persistence;

import org.monet.space.kernel.model.Fact;

public interface HistoryLog {

	void init(String id);

	void add(String data);

	void add(Fact data);

}
