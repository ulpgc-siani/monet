package org.monet.space.kernel.agents;

import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.machines.ttm.impl.MessageQueueServiceMonet.MessageQueueRetryWork;
import org.monet.space.kernel.model.Context;
import org.monet.space.kernel.model.WorkQueueItem;
import org.monet.space.kernel.producers.ProducerWorkQueue;
import org.monet.space.kernel.producers.ProducersFactory;
import org.monet.space.kernel.workqueue.WorkQueueAction;
import org.monet.space.kernel.workqueue.WorkQueueActionFactory;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AgentWorkQueue extends TimerTask {
	private static final int SECOND = 1000;
	private static final int MINUTE = SECOND * 60;
	private static final int HOUR = MINUTE * 60;
	private static final int DAY = HOUR * 24;
	private static AgentWorkQueue instance;

	private Configuration configuration;
	private ProducerWorkQueue producer;
	private Timer managerTimer;

	public synchronized static AgentWorkQueue getInstance() {
		if (instance == null) {
			instance = new AgentWorkQueue();
		}
		return instance;
	}

    public synchronized static boolean started() {
        return instance != null;
    }

	private AgentWorkQueue() {
		this.configuration = Configuration.getInstance();
		this.producer = (ProducerWorkQueue) ProducersFactory.getInstance().get(Producers.WORKQUEUE);
	}

	public synchronized void init() {
		this.managerTimer = new Timer(String.format("Monet-WorkQueue-%s", Context.getInstance().getFrameworkName()), true);
		this.managerTimer.schedule(this, 1000, this.configuration.getWorkQueuePeriod() * 1000);
		AgentLogger.getInstance().info("Monet-WorkQueue-%s created", Context.getInstance().getFrameworkName());
	}

	public synchronized void queueNewWork(WorkQueueItem workQueueItem) {
		this.producer.queueNew(workQueueItem);
	}

	@Override
	public synchronized void run() {
		try {
			WorkQueueActionFactory factory = WorkQueueActionFactory.getInstance();
			List<WorkQueueItem> items = this.producer.loadPending();
			for (WorkQueueItem item : items) {
				try {

					if (!execute(item))
						continue;

					WorkQueueAction action = factory.build(item.getType());
					action.execute(item);
					this.producer.updateToFinish(item.getId());
				} catch (Throwable e) {
					StringBuilder builder = new StringBuilder();
					builder.append(e.getMessage());
					builder.append("\n");
					for (StackTraceElement element : e.getStackTrace()) {
						builder.append(element.toString());
						builder.append("\n");
					}

					this.producer.updateWithError(item.getId(), builder.toString());
				}
			}
			MessageQueueRetryWork messageQueueWork = new MessageQueueRetryWork();
			messageQueueWork.execute(null);
		} catch (Exception e) {
		}
	}

	private boolean execute(WorkQueueItem item) {
		int retries = item.getRetries();
		Date lastUpdateTime = item.getLastUpdateTime();
		Date now = new Date();
		long difference = now.getTime() - lastUpdateTime.getTime();

		if (retries < 10 && difference > SECOND)
			return true;
		else if (retries < 100 && difference > MINUTE)
			return true;
		else if (retries < 1000 && difference > HOUR)
			return true;
		else if (retries >= 1000 && difference > DAY)
			return true;

		return false;
	}

	public synchronized void destroy() {
		if (managerTimer != null) {
			this.managerTimer.cancel();
			this.managerTimer.purge();
		}
	}

}
