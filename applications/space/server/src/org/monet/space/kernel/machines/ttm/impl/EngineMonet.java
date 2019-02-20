package org.monet.space.kernel.machines.ttm.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.monet.metamodel.ProcessDefinition;
import org.monet.space.kernel.machines.ttm.Engine;
import org.monet.space.kernel.machines.ttm.behavior.ProcessBehavior;
import org.monet.space.kernel.machines.ttm.model.Process;
import org.monet.space.kernel.machines.ttm.persistence.PersistenceService;

import java.lang.ref.SoftReference;
import java.util.HashMap;

@Singleton
public class EngineMonet implements Engine {

	@Inject
	private PersistenceService persistenceService;
	@Inject
	private Provider<ProcessBehavior> processBehaviorFactory;

	private HashMap<String, SoftReference<ProcessBehavior>> processCache = new HashMap<String, SoftReference<ProcessBehavior>>();

	@Override
	public synchronized ProcessBehavior getProcess(String processId) {
		if (this.processCache.containsKey(processId)) {
			ProcessBehavior process = this.processCache.get(processId).get();
			if (process != null) {
				return process;
			}
		}

		Process process = this.persistenceService.loadProcess(processId);
		if (process == null)
			return null;

		ProcessBehavior behavior = processBehaviorFactory.get();
		behavior.injectModel(process);
		this.processCache.put(processId, new SoftReference<ProcessBehavior>(behavior));

		return behavior;
	}

	@Override
	public synchronized ProcessBehavior buildProcess(String taskId, ProcessDefinition definition) {
		ProcessBehavior behavior = processBehaviorFactory.get();
		Process process = new Process(taskId, definition);
		behavior.injectModel(process);

		this.processCache.put(taskId, new SoftReference<ProcessBehavior>(behavior));

		try {
			behavior.init();
		} catch (Exception e) {
			this.processCache.remove(taskId);
			throw new RuntimeException(e);
		}

		return behavior;
	}

}
