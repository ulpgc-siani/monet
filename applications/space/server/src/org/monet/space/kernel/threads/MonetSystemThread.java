package org.monet.space.kernel.threads;

import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.AgentSession;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.constants.ApplicationInterface;
import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.model.Context;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public class MonetSystemThread {

	public static final String SYSTEM_USER_ID = "-1";

	private static Integer instances = 0;

	private Thread t;
	private Runnable taskToRun;

	private Runnable internal = new Runnable() {

		private String idTransportSession;
		private Context oContext;

		private void updateContext() {
			this.oContext = Context.getInstance();
			long idCurrentThread = Thread.currentThread().getId();
			oContext.setApplication(idCurrentThread, "localhost", "core", ApplicationInterface.APPLICATION);
			oContext.setSessionId(idCurrentThread, this.idTransportSession);
			oContext.setDatabaseConnectionType(idCurrentThread, Database.ConnectionTypes.AUTO_COMMIT);
		}

		@Override
		public void run() {
			//Prepare thread with System user data
			AgentSession agentSession = AgentSession.getInstance();
			this.idTransportSession = UUID.randomUUID().toString();
			agentSession.add(this.idTransportSession);
			try {
				updateContext();
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
				});
				taskToRun.run();
			} catch (Throwable e) {
				AgentLogger.getInstance().error(e);
			} finally {
				Context.getInstance().clear(Thread.currentThread().getId());
				agentSession.remove(this.idTransportSession);
			}
		}
	};

	public MonetSystemThread(Runnable runnable) {
		int n;
		synchronized (instances) {
			n = instances++;
		}
		this.t = new Thread(this.internal, "MonetSystemThread-" + n);
		this.taskToRun = runnable;
	}

	public void start() {
		t.start();
	}


}
