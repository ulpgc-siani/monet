package org.monet.space.kernel.machines.ttm;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.*;
import org.monet.space.kernel.machines.ttm.TimerService.TimerServiceCallback;
import org.monet.space.kernel.machines.ttm.behavior.ContestBehavior;
import org.monet.space.kernel.machines.ttm.behavior.CustomerBehavior;
import org.monet.space.kernel.machines.ttm.behavior.ProcessBehavior;
import org.monet.space.kernel.machines.ttm.behavior.ProviderBehavior;
import org.monet.space.kernel.machines.ttm.impl.*;
import org.monet.space.kernel.machines.ttm.persistence.HistoryLog;
import org.monet.space.kernel.machines.ttm.persistence.PersistenceService;
import org.monet.space.kernel.machines.ttm.persistence.SnapshotStack;

public class TaskTransitionMachineModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(ContestBehavior.class);
		bind(CustomerBehavior.class);
		bind(ProcessBehavior.class);
		bind(ProviderBehavior.class);

		bind(Engine.class).to(EngineMonet.class);
		bind(CourierService.class).to(CourierServiceMonet.class);
		bind(PersistenceService.class).to(PersistenceServiceMonet.class);
		bind(MessageQueueService.class).to(MessageQueueServiceMonet.class);
		bind(TimerService.class).to(TimerServiceMonet.class);
		bind(TimerServiceCallback.class).to(TimerServiceMonet.class);
		bind(HistoryLog.class).to(HistoryLogMonet.class);
		bind(SnapshotStack.class).to(SnapshotStackMonet.class);
	}

	@Provides
	public TaskLayer taskLayerProducer() {
		return ComponentPersistence.getInstance().getTaskLayer();
	}

	@Provides
	public RoleLayer roleLayerProducer() {
		return ComponentFederation.getInstance().getRoleLayer();
	}

	@Provides
	public NodeLayer nodeLayerProducer() {
		return ComponentPersistence.getInstance().getNodeLayer();
	}

	@Provides
	public MessageQueueLayer messageQueueLayerProducer() {
		return ComponentPersistence.getInstance().getMessageQueueLayer();
	}

	@Provides
	public MailBoxLayer mailBoxLayerProducer() {
		return ComponentPersistence.getInstance().getMailBoxLayer();
	}

}
