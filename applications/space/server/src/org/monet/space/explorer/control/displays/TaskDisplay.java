package org.monet.space.explorer.control.displays;

import org.monet.space.explorer.control.displays.serializers.Serializer;
import org.monet.space.explorer.control.displays.serializers.TaskSerializer;
import org.monet.space.kernel.model.Task;

public class TaskDisplay extends HttpDisplay<Task> {

	@Override
	protected Serializer getSerializer(Task object) {
		return new TaskSerializer(createSerializerHelper());
	}

}
