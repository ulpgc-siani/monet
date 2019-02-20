package org.monet.bpi.java;

import org.monet.api.space.backservice.BackserviceApi;
import org.monet.bpi.Job;
import org.monet.bpi.JobService;
import org.monet.v3.BPIClassLocator;
import org.monet.metamodel.TaskDefinition;
import org.monet.v3.model.Dictionary;

public class JobServiceImpl extends JobService {
	private BackserviceApi api;
	private Dictionary dictionary;
	private BPIClassLocator bpiClassLocator;

	public static void injectInstance(JobServiceImpl service) {
		instance = service;
	}

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	public void injectDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}

	@Override
	public Job getImpl(String id) {
		org.monet.api.space.backservice.impl.model.Task task = this.api.openTask(id);
		JobImpl bpiJob = new JobImpl();
		bpiJob.injectTask(task);
		bpiJob.injectDictionary(this.dictionary);
		bpiJob.injectBPIClassLocator(this.bpiClassLocator);
		bpiJob.injectApi(this.api);
		return bpiJob;
	}

	@Override
	public Job createImpl(Class<? extends Job> clazz) {
		TaskDefinition definition = (TaskDefinition)this.dictionary.getDefinition(clazz.getName());
		org.monet.api.space.backservice.impl.model.Task task = this.api.createTask(definition.getCode());
		JobImpl bpiJob = new JobImpl();
		bpiJob.injectTask(task);
		bpiJob.injectDictionary(this.dictionary);
		bpiJob.injectBPIClassLocator(this.bpiClassLocator);
		bpiJob.injectApi(this.api);
		return bpiJob;
	}

	public static void init() {
	}

}
