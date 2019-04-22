package org.monet.space.kernel.agents;

import org.monet.http.Request;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.EventLayer;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.constants.ApplicationInterface;
import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Context;
import org.monet.space.kernel.model.Event;
import org.monet.space.kernel.model.EventList;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class AgentEvents extends TimerTask {
	private ScheduledExecutorService scheduler;
	private EventLayer eventLayer;
	private Timer managerTimer;
	private String idTransportSession;
	private Map<String, ScheduledFuture> scheduledEvents = new HashMap<>();

	private static final int MINUTE = 60 * 1000;
	private static final int HOUR = 60 * MINUTE;
	private static AgentEvents instance;

	public synchronized static AgentEvents getInstance() {
		if (instance == null)
			instance = new AgentEvents();
		return instance;
	}

    public synchronized static boolean started() {
        return instance != null;
    }

	private AgentEvents() {
		scheduler = Executors.newScheduledThreadPool(100);
		this.eventLayer = ComponentPersistence.getInstance().getEventLayer();
	}

	public synchronized void init() {
		Configuration configuration = Configuration.getInstance();

		this.managerTimer = new Timer(String.format("Monet-Event-%s", Context.getInstance().getFrameworkName()), true);
		this.managerTimer.schedule(this, 1000, configuration.isDebugMode()?MINUTE:HOUR);
	}

	@Override
	public synchronized void run() {

		try {
			EventList eventList = eventLayer.loadDueEvents();

			for (final Event event : eventList) {
				if (scheduledEvents.containsKey(event.getName()))
					continue;

				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						prepareThreadSession();

						eventLayer.notifyEvent(event);
						scheduledEvents.remove(event.getName());
					}
				};

				long nowTime = new Date().getTime();
				long eventTime = event.getDueDate().getTime();
				long delay = (eventTime > nowTime) ? eventTime - nowTime : 0;

				ScheduledFuture scheduledEvent = scheduler.schedule(runnable, delay, TimeUnit.MILLISECONDS);
				scheduledEvents.put(event.getName(), scheduledEvent);
			}
		}
		catch (Exception exception) {
			AgentLogger.getInstance().error(exception);
		}
	}

	private void prepareThreadSession() {
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
			public Request getRequest() {
				return null;
			}
		}).loginAsSystem();
	}

	private void updateContext() {
		Context context = Context.getInstance();
		long idCurrentThread = Thread.currentThread().getId();
		context.setApplication(idCurrentThread, "localhost", "core", ApplicationInterface.APPLICATION);
		context.setSessionId(idCurrentThread, this.idTransportSession);
		context.setDatabaseConnectionType(idCurrentThread, Database.ConnectionTypes.AUTO_COMMIT);
	}

	public synchronized void destroy() {
		if (managerTimer != null) {
			this.managerTimer.cancel();
			this.managerTimer.purge();
		}
	}

}
