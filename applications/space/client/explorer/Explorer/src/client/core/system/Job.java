package client.core.system;

import client.core.model.TaskView;
import client.core.model.ViewList;
import client.core.model.definition.entity.JobDefinition;

public class Job<Definition extends JobDefinition> extends Task<Definition> implements client.core.model.Job<Definition> {

	public Job() {
	}

	public Job(String id, String label) {
		super(id, label);
	}

	@Override
	protected ViewList<TaskView> loadViews() {
		return new client.core.system.ViewList<>();
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.Job.CLASS_NAME;
	}

}
