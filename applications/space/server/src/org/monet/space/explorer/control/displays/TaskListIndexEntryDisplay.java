package org.monet.space.explorer.control.displays;

import org.monet.space.explorer.control.displays.serializers.Serializer;
import org.monet.space.explorer.control.displays.serializers.TaskListIndexEntrySerializer;
import org.monet.space.explorer.model.TaskListIndexEntry;

public class TaskListIndexEntryDisplay extends HttpDisplay<TaskListIndexEntry> {

	@Override
	protected Serializer getSerializer(TaskListIndexEntry object) {
		return new TaskListIndexEntrySerializer(createSerializerHelper());
	}

}
