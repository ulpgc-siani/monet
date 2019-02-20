package org.monet.space.kernel.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.AgentServiceClient;
import org.monet.space.kernel.deployer.stages.InstanceAndRefreshDashboards;
import org.monet.space.kernel.deployer.stages.InstanceAndRefreshDatastores;
import org.monet.space.kernel.log.LoggerModule;
import org.monet.space.kernel.machines.MachinesModule;
import org.monet.space.kernel.machines.bim.BusinessIntelligenceMachineModule;
import org.monet.space.kernel.machines.ttm.TaskTransitionMachineModule;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

public class CoreModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new LoggerModule());
		install(new MachinesModule());
		install(new BusinessIntelligenceMachineModule());
		install(new TaskTransitionMachineModule());

		bind(InstanceAndRefreshDatastores.class);
		bind(InstanceAndRefreshDashboards.class);

		bind(AgentServiceClient.class).toInstance(AgentServiceClient.getInstance());
		bind(AgentLogger.class).toInstance(AgentLogger.getInstance());
	}

	@Provides
	public XmlSerializer getXmlSerializer() throws XmlPullParserException {
		return XmlPullParserFactory.newInstance().newSerializer();
	}

}
