package org.monet.space.kernel.machines.ttm.model;

import org.monet.space.kernel.machines.ttm.TimerService.TimerServiceCallback;

import java.util.Date;
import java.util.TimerTask;

public class Timer {

	private String id;
	private String taskId;
	private String tag;
	private Date created;
	private long delay;
	private TimerTask timer;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public TimerTask getTimer() {
		return timer;
	}

	public void activate(final TimerServiceCallback callback) {
		this.timer = new TimerTask() {

			@Override
			public void run() {
				callback.timeout(Timer.this);
			}
		};

		Date current = new Date();
		long pastTime = current.getTime() - this.created.getTime();
		long realDelay = this.delay - pastTime;

		if (realDelay < 45000)
			realDelay = 45000; // 45 seconds to let the application start completely
		else {
			if (realDelay < 0)
				realDelay = 0;
		}

		callback.schedule(timer, realDelay);
	}

}
