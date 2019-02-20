package org.monet.bpi.java;

import org.monet.bpi.Job;
import org.monet.bpi.JobService;
import org.monet.metamodel.TaskDefinition;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.model.BusinessUnit;

public class JobServiceImpl extends JobService {

	BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();

	@Override
	public Job getImpl(String id) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		org.monet.space.kernel.model.Task task = taskLayer.loadTask(id);
		JobImpl bpiJob = new JobImpl();
		((JobImpl) bpiJob).injectTask(task);
		return bpiJob;
	}

	@Override
	public Job createImpl(Class<? extends Job> clazz) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		TaskDefinition definition = BusinessUnit.getInstance().getBusinessModel().getDictionary().getTaskDefinition(clazz.getName());
		org.monet.space.kernel.model.Task task = taskLayer.createTask(definition.getCode());
		JobImpl bpiJob = new JobImpl();
		((JobImpl) bpiJob).injectTask(task);
		return bpiJob;
	}

	public static void init() {
		instance = new JobServiceImpl();
	}

}
