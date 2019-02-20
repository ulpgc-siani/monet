package org.monet.space.kernel.components.layers;

import org.monet.space.kernel.model.EventLog;
import org.monet.space.kernel.model.EventLogList;

import java.util.List;

public interface EventLogLayer extends Layer {

	public void insertEventLogBlock(List<EventLog> eventLogs);

	public EventLogList loadEventLogPage(String logger, int offset, int pageSize);

	public void clearEventLog(String logger);

}
