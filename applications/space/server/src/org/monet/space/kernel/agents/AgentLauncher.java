package org.monet.space.kernel.agents;

import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.constants.ApplicationInterface;
import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Context;

import javax.servlet.http.HttpServletRequest;
import java.util.TimerTask;
import java.util.UUID;

public class AgentLauncher extends TimerTask {
	private java.util.Timer timer;

	private static AgentLauncher instance;
	private static boolean launched = false;
	private String idTransportSession;

	private AgentLauncher() {
	}

	public static AgentLauncher getInstance() {
		if (instance == null) instance = new AgentLauncher();
		return instance;
	}

	public synchronized void init() {
		this.timer = new java.util.Timer("Monet-Space.Launcher", true);
		this.timer.schedule(this, 1, 3000);
	}

	@Override
	public void run() {
		AgentLogger agentLogger = AgentLogger.getInstance();

		agentLogger.info("Launching %s agents", Context.getInstance().getFrameworkName());

		if (launched) {
			this.cancel();
			this.timer.cancel();
			return;
		}

		try {
			AgentSession agentSession = AgentSession.getInstance();
			this.idTransportSession = UUID.randomUUID().toString();
			agentSession.add(this.idTransportSession);

			updateContext();

			if (BusinessUnit.getInstance().isInstalled())
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

			AgentWorkQueue.getInstance().init();
			AgentSourceUpgrade.getInstance().init();
			AgentDatawareHouseUpgrade.getInstance().init();
			AgentEvents.getInstance().init();
			launched = true;

			agentLogger.info("Launching %s agents... done :)", Context.getInstance().getFrameworkName());
		}
		catch (Throwable exception) {
			agentLogger.error(String.format("Launching %s agents... delayed :(", Context.getInstance().getFrameworkName()), exception);
		}
		finally {
			Context.getInstance().clear(Thread.currentThread().getId());
		}

	}

	private void updateContext() {
		Context context = Context.getInstance();
		long idCurrentThread = Thread.currentThread().getId();
		context.setApplication(idCurrentThread, "localhost", "core", ApplicationInterface.APPLICATION);
		context.setSessionId(idCurrentThread, this.idTransportSession);
		context.setDatabaseConnectionType(idCurrentThread, Database.ConnectionTypes.AUTO_COMMIT);
	}

	public synchronized void destroy() {
		if (timer != null) {
			this.timer.cancel();
			this.timer.purge();
		}
	}

}
