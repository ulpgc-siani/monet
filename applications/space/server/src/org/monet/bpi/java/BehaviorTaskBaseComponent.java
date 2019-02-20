package org.monet.bpi.java;

import org.monet.bpi.Task;

public class BehaviorTaskBaseComponent {

	Task task;

	void injectTask(Task task) {
		this.task = task;
	}

	protected Task getGenericTask() {
		return this.task;
	}

}
