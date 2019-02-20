package org.monet.space.kernel.machines.ttm.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.monet.metamodel.internal.Time;
import org.monet.space.kernel.machines.ttm.Engine;
import org.monet.space.kernel.machines.ttm.TimerService;
import org.monet.space.kernel.machines.ttm.TimerService.TimerServiceCallback;
import org.monet.space.kernel.machines.ttm.behavior.ProcessBehavior;
import org.monet.space.kernel.machines.ttm.model.Timer;
import org.monet.space.kernel.machines.ttm.persistence.PersistenceService;
import org.monet.space.kernel.threads.MonetSystemThread;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

@Singleton
public class TimerServiceMonet implements TimerService, TimerServiceCallback {

	@Inject
	private PersistenceService persistenceService;

	@Inject
	private Engine engine;

	private boolean initialized = false;
	private java.util.Timer timer;
	private HashMap<String, HashMap<String, Timer>> scheduledTimers = new HashMap<String, HashMap<String, Timer>>();

	public TimerServiceMonet() {
		this.timer = new java.util.Timer("TimerServiceMonet", true);
	}

	@Override
	public void init() {
		if (initialized)
			return;

		List<Timer> timerList = this.persistenceService.loadAllTimers();
		for (Timer timer : timerList) {
			this.addAndActivate(timer);
		}
	}

	@Override
	public boolean isActive(String taskId, String tag) {
		HashMap<String, Timer> taskTimers = this.scheduledTimers.get(taskId);
		if (taskTimers != null) {
			Timer timer = taskTimers.get(tag);
			return timer != null;
		}
		return false;
	}

	private void addAndActivate(Timer timer) {
		HashMap<String, Timer> taskTimers = scheduledTimers.get(timer.getTaskId());
		if (taskTimers == null) {
			taskTimers = new HashMap<String, Timer>();
			scheduledTimers.put(timer.getTaskId(), taskTimers);
		}
		taskTimers.put(timer.getTag(), timer);
		timer.activate(this);
	}

	@Override
	public long getTimerDue(String taskId, String tag) {
		HashMap<String, Timer> taskTimers = this.scheduledTimers.get(taskId);
		if (taskTimers != null) {
			Timer timer = taskTimers.get(tag);
			if (timer == null) return -1;
			return timer.getCreated().getTime() + timer.getDelay();
		}
		return -1;
	}

	@Override
	public void schedule(String taskId, String tag, Date dueDate, Time delay) {
		if (this.isActive(taskId, tag))
			this.cancel(taskId, tag);

		Timer timer = new Timer();
		timer.setTaskId(taskId);
		timer.setTag(tag);
		timer.setCreated(dueDate);
		timer.setDelay(delay.getTime());
		this.persistenceService.create(timer);

		this.addAndActivate(timer);
	}

	@Override
	public void cancel(String taskId, String tag) {
		HashMap<String, Timer> taskTimers = this.scheduledTimers.get(taskId);
		if (taskTimers != null) {
			Timer timer = taskTimers.remove(tag);
			if (timer != null) {
				timer.getTimer().cancel();
				this.persistenceService.delete(timer);
			}
		}
	}

	@Override
	public void cancelAll(String taskId) {
		HashMap<String, Timer> taskTimers = this.scheduledTimers.get(taskId);
		for (Timer timer : taskTimers.values()) {
			timer.getTimer().cancel();
			this.persistenceService.delete(timer);
		}
		taskTimers.clear();
	}

	@Override
	public void timeout(final Timer timer) {
		MonetSystemThread systemThread = new MonetSystemThread(new Runnable() {

			@Override
			public void run() {
				ProcessBehavior process = TimerServiceMonet.this.engine.getProcess(timer.getTaskId());
				process.onTimeout(timer.getTag());
			}
		});
		systemThread.start();
	}

	@Override
	public void schedule(TimerTask timerTask, long delay) {
		this.timer.schedule(timerTask, delay);
	}

}
