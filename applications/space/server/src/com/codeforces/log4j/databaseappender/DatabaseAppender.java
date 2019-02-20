package com.codeforces.log4j.databaseappender;

import com.codeforces.util.ThrowableUtil;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.EventLogLayer;
import org.monet.space.kernel.model.Context;
import org.monet.space.kernel.model.EventLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class DatabaseAppender extends AppenderSkeleton {
	private static final int CACHE_TIMER = 5000;
	private static final BlockingQueue<LoggingEvent> loggingEventQueue = new LinkedBlockingDeque<LoggingEvent>();

	private static DatabaseAppender instance;
	private static Timer timer;
	private static boolean stop = false;
	private static Thread thread;

	private ArrayList<EventLog> cache = new ArrayList<EventLog>();
	private EventLogLayer eventLogLayer;

	public DatabaseAppender() {
		super();
		instance = this;
		this.eventLogLayer = ComponentPersistence.getInstance().getEventLogLayer();
	}

	@Override
	protected void append(LoggingEvent event) {
		loggingEventQueue.add(event);
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

	private void processEvent(LoggingEvent loggingEvent) {
		EventLog eventLog = new EventLog();
		if (loggingEvent.getThrowableInformation() != null) {
			eventLog.setStacktrace(ThrowableUtil.getStacktrace(loggingEvent.getThrowableInformation().getThrowable()));
		}

		eventLog.setLogger(loggingEvent.getLoggerName());
		eventLog.setPriority(loggingEvent.getLevel().toString());
		eventLog.setMessage(loggingEvent.getMessage().toString());
		eventLog.setCreationTime(new Date(loggingEvent.getTimeStamp()));

		synchronized (this.cache) {
			this.cache.add(eventLog);
		}
	}

	protected void persistCache() {
		synchronized (this.cache) {
			if (this.cache.size() == 0) return;
			this.eventLogLayer.insertEventLogBlock(this.cache);
			this.cache.clear();
		}
	}

	@Override
	public void close() {
		this.closed = true;
	}

	private static void processQueue() {
		while (!stop) {
			try {
				LoggingEvent event = loggingEventQueue.poll(1L, TimeUnit.SECONDS);
				if (event != null) instance.processEvent(event);
			} catch (Exception e) {
				// No operations.
			}
		}
	}

	static {
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				processQueue();
			}
		});
		thread.setDaemon(true);
		thread.start();
		timer = new Timer(String.format("DatabaseAppender-%s", Context.getInstance().getFrameworkName()));
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				instance.persistCache();
			}
		}, 1000, CACHE_TIMER);
	}

	public static void destroy() {
		stop = true;
		thread.interrupt();
		timer.cancel();
		timer.purge();
	}
}
