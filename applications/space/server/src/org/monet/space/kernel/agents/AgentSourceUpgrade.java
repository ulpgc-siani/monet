package org.monet.space.kernel.agents;

import org.monet.metamodel.SourceDefinition;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.components.layers.SourceLayer;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.constants.ApplicationInterface;
import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Context;
import org.monet.space.kernel.model.Source;
import org.monet.space.kernel.model.SourceList;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class AgentSourceUpgrade extends TimerTask {
	private java.util.Timer timer;
	private Configuration configuration;
	private AgentLogger agentLogger;
	private String idTransportSession;

	private static AgentSourceUpgrade instance;

	private AgentSourceUpgrade() {
		this.configuration = Configuration.getInstance();
		this.agentLogger = AgentLogger.getInstance();
	}

	public static AgentSourceUpgrade getInstance() {
		if (instance == null)
			instance = new AgentSourceUpgrade();
		return instance;
	}

	public synchronized void init() {
		this.timer = new java.util.Timer("Monet-Source.Upgrade", true);

        if (this.configuration.launchSourceUpgradeOnStart())
            this.upgrade();

		this.timer.schedule(this, 30000, 60*30*1000);
	}

	@Override
	public void run() {
        if (!this.isUpgradeHour())
            return;
        this.upgrade();
	}

    private void upgrade() {
        this.agentLogger.info(String.format("Starting %s source upgrade", Context.getInstance().getFrameworkName()));

        try {
            AgentSession agentSession = AgentSession.getInstance();
            this.idTransportSession = UUID.randomUUID().toString();
            agentSession.add(this.idTransportSession);

            updateContext();

            if (!BusinessUnit.getInstance().isInstalled())
                return;

            ComponentFederation.getInstance().getLayer(new FederationLayer.Configuration() {
	            @Override
	            public String getSessionId() {
		            return idTransportSession;
	            }

	            @Override
	            public String getCallbackUrl() {
		            return "http://localhost";
	            }

	            @Override
	            public String getLogoUrl() {
		            return null;
	            }

	            @Override
	            public HttpServletRequest getRequest() {
		            return null;
	            }
            }).loginAsSystem();

            SourceLayer sourceLayer = ComponentPersistence.getInstance().getSourceLayer();
            SourceList sourceList = sourceLayer.loadGlossaryList();

            for (Source<SourceDefinition> source : sourceList) {
	            try {
		            sourceLayer.synchronizeSource(source);
	            }
	            catch (Exception e) {
		            Timer timer = new Timer("Monet-Source-Upgrade-" + source.getName());
		            timer.schedule(new TimerTask() {
			            @Override
			            public void run() {
				            upgrade();
			            }
		            }, 10000);
		            this.agentLogger.info("Source %s of space %s is down! Trying later...", source.getLabel(), source.getPartnerLabel());
	            }
            }

        } catch (Exception e) {
	        this.agentLogger.error(e.getMessage(), e);
        }
        finally {
            Context.getInstance().clear(Thread.currentThread().getId());
        }

        this.agentLogger.info(String.format("%s source upgrade finished", Context.getInstance().getFrameworkName()));
    }

    private boolean isUpgradeHour() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return hour == this.configuration.getSourceUpgradeHour();
    }

    private void updateContext() {
		Context context = Context.getInstance();
		long idCurrentThread = Thread.currentThread().getId();
		context.setApplication(idCurrentThread, "localhost", "core", ApplicationInterface.APPLICATION);
		context.setSessionId(idCurrentThread, this.idTransportSession);
		context.setDatabaseConnectionType(idCurrentThread, Database.ConnectionTypes.AUTO_COMMIT);
	}

}
