package org.monet.bpi.java;

import org.monet.api.space.backservice.BackserviceApi;
import org.monet.bpi.Task;
import org.monet.bpi.TaskService;
import org.monet.v3.BPIClassLocator;
import org.monet.metamodel.Definition;
import org.monet.metamodel.TaskDefinition;
import org.monet.v3.model.Dictionary;

public class TaskServiceImpl extends TaskService {
	private BackserviceApi api;
	private Dictionary dictionary;
	private BPIClassLocator bpiClassLocator;

	public static void injectInstance(TaskServiceImpl service) {
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
	public Task getImpl(String taskId) {
		org.monet.api.space.backservice.impl.model.Task task = this.api.openTask(taskId);
		Definition definition = this.dictionary.getDefinition(task.getCode());
		TaskImpl bpiTask = (TaskImpl) this.bpiClassLocator.instantiateBehaviour(definition);
		bpiTask.injectTask(task);
		bpiTask.injectBPIClassLocator(this.bpiClassLocator);
		bpiTask.injectApi(this.api);
		bpiTask.injectDictionary(this.dictionary);
		return bpiTask;
	}

	@Override
	public Task createImpl(Class<? extends Task> taskClass) {
		TaskDefinition definition = (TaskDefinition)this.dictionary.getDefinition(taskClass.getName());
		org.monet.api.space.backservice.impl.model.Task task = this.api.createTask(definition.getCode());
		TaskImpl bpiTask = (TaskImpl) this.bpiClassLocator.instantiateBehaviour(definition);
		bpiTask.injectTask(task);
		bpiTask.injectBPIClassLocator(this.bpiClassLocator);
		bpiTask.injectApi(this.api);
		bpiTask.injectDictionary(this.dictionary);
		return bpiTask;
	}

	public static void init() {
		instance = new TaskServiceImpl();
	}

}
