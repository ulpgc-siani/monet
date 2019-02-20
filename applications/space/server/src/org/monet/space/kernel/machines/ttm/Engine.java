package org.monet.space.kernel.machines.ttm;

import org.monet.metamodel.ProcessDefinition;
import org.monet.space.kernel.machines.ttm.behavior.ProcessBehavior;

public interface Engine {

	ProcessBehavior getProcess(String taskId);

	ProcessBehavior buildProcess(String taskId, ProcessDefinition definition);

}
