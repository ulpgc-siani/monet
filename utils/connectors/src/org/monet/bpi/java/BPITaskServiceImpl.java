package org.monet.bpi.java;

import org.monet.api.backservice.BackserviceApi;
import org.monet.api.backservice.impl.model.Task;
import org.monet.bpi.BPITask;
import org.monet.bpi.BPITaskService;
import org.monet.v2.BPIClassLocator;
import org.monet.v2.metamodel.Definition;
import org.monet.v2.model.Dictionary;

import java.lang.annotation.Annotation;

public class BPITaskServiceImpl extends BPITaskService {
	private BackserviceApi api;
	private BPIClassLocator bpiClassLocator;
	private Dictionary dictionary;

	public static void injectInstance(BPITaskServiceImpl service) {
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

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public <T extends BPITask<?, ?, ?, ?>> T get(String taskId) {
		Task task = this.api.openTask(taskId);
		Definition definition = this.dictionary.getDefinition(task.getCode());
		BPITaskImpl bpiTask = (BPITaskImpl) this.bpiClassLocator.getDefinitionInstance(definition.getName());
		bpiTask.injectTask(task);
		bpiTask.injectApi(this.api);
		bpiTask.injectBPIClassLocator(this.bpiClassLocator);
		bpiTask.injectDictionary(this.dictionary);
		return (T) bpiTask;
	}

	@Override
	public <T extends BPITask<?, ?, ?, ?>> T add(Class<T> taskClass) {
		String name = getDefinitionName(taskClass);
		return add(name);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public <T extends BPITask<?, ?, ?, ?>> T add(String name) {
		String code = this.dictionary.getTaskDefinition(name).getCode();
		Task task = this.api.createTask(code);
		BPITaskImpl bpiTask = (BPITaskImpl) this.bpiClassLocator.getDefinitionInstance(name);
		bpiTask.injectTask(task);
		bpiTask.injectApi(this.api);
		bpiTask.injectBPIClassLocator(this.bpiClassLocator);
		bpiTask.injectDictionary(this.dictionary);
		return (T) bpiTask;
	}

	private <T> String getDefinitionName(Class<T> clazz) {
		try {
			for (Annotation annotation : clazz.getAnnotations()) {
				if (annotation instanceof Definition) {
					return ((Definition) annotation).getName();
				}
			}
		} catch (Exception e) {
		}

		return null;
	}

}
