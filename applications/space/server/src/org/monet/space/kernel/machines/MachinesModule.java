package org.monet.space.kernel.machines;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.monet.space.kernel.agents.AgentNotifier;

public class MachinesModule extends AbstractModule {

	@Override
	protected void configure() {
	}

	@Provides
	public AgentNotifier agentNotifierProducer() {
		return AgentNotifier.getInstance();
	}

}
