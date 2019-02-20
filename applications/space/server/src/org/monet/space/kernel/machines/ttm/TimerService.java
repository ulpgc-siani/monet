package org.monet.space.kernel.machines.ttm;

import org.monet.metamodel.internal.Time;
import org.monet.space.kernel.machines.ttm.model.Timer;

import java.util.Date;
import java.util.TimerTask;

public interface TimerService {

	void init();

	boolean isActive(String taskId, String tag);

	long getTimerDue(String id, String name);

	void schedule(String taskId, String tag, Date dueDate, Time delay);

	void cancel(String taskId, String tag);

	void cancelAll(String taskId);

	public interface TimerServiceCallback {

		void timeout(Timer timer);

		void schedule(TimerTask timerTask, long delay);

	}


}
