package org.monet.docservice.docprocessor.worker.impl;

import com.google.inject.Inject;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.configuration.Configuration;
import org.monet.docservice.docprocessor.operations.Operation;
import org.monet.docservice.docprocessor.operations.OperationsFactory;
import org.monet.docservice.docprocessor.worker.WorkQueueItem;
import org.monet.docservice.docprocessor.worker.WorkQueueManager;
import org.monet.docservice.docprocessor.worker.WorkQueueRepository;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class WorkQueueManagerImpl extends TimerTask implements WorkQueueManager {

	private Logger logger;
	private Configuration configuration;
	private WorkQueueRepository workQueueRepository;
	private OperationsFactory operationsFactory;

	private Timer managerTimer;
	private ExecutorService threadPool;

	@Inject
	public WorkQueueManagerImpl(Logger logger,
	                            Configuration configuration,
	                            WorkQueueRepository workQueueRepository) {
		logger.debug("WorkQueueManagerImpl(%s)", logger);

		this.logger = logger;
		this.configuration = configuration;
		this.workQueueRepository = workQueueRepository;
	}

	@Inject
	public void injectOperationsFactory(OperationsFactory operationsFactory) {
		this.operationsFactory = operationsFactory;
	}

	@Override
	public void init() {
		logger.debug("init()");

		int threadPoolSize = this.configuration.getInt(Configuration.WORKQUEUE_THREADPOOL_SIZE);
		this.threadPool = Executors.newFixedThreadPool(threadPoolSize, new ThreadFactory() {

			public Thread newThread(Runnable r) {
				Thread t = Executors.defaultThreadFactory().newThread(r);
				t.setName("DocServerWorkerThread");
				return t;
			}
		});
		this.managerTimer = new Timer("WorkQueueManager", true);
		this.managerTimer.schedule(this,
			1000,
			this.configuration
				.getInt(Configuration.WORKQUEUE_THREADPOOL_PERIOD) * 1000
		);
		this.workQueueRepository.resetAllInProgress();
	}

	public void run() {
		logger.debug("Starting to collect new work to do");
		try {
			List<WorkQueueItem> items = this.workQueueRepository.getNotStartedOperations();
			logger.debug("Found %d work queue items", items.size());
			for (WorkQueueItem item : items) {
				WorkerThread wt = new WorkerThread(item);
				this.workQueueRepository.updateWorkQueueItemToPending(item.getId());
				this.threadPool.execute(wt);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		logger.debug("Finishing collecting");
	}

	class WorkerThread implements Runnable {

		private WorkQueueItem item;

		public WorkerThread(WorkQueueItem item) {
			this.item = item;
		}

		public void run() {
			logger.info("Starting thread %d, working with %d", Thread.currentThread().getId(), this.item.getId());

			try {
				workQueueRepository.updateWorkQueueItemToInProgress(this.item.getId());

				Operation op = WorkQueueManagerImpl.this.operationsFactory.create(this.item.getOperation());
				item.setExtraDataInputStream(WorkQueueManagerImpl.this.workQueueRepository.getWorkQueueItemExtraData(this.item.getId()));
				op.setTarget(item);
				op.execute();

				workQueueRepository.updateWorkQueueItemToFinished(this.item.getId());
			} catch (Throwable e) {
				logger.error(e.getMessage(), e);
				workQueueRepository.updateWorkQueueItemToError(this.item.getId(), e.getMessage());
			}

			logger.info("Thread %d finish working with %d", Thread.currentThread().getId(), this.item.getId());
		}

	}


	@Override
	public void destroy() {
		logger.info("Stoping threads");
		this.managerTimer.cancel();
		this.managerTimer.purge();
		try {
			this.threadPool.awaitTermination(5, TimeUnit.SECONDS);
			if (!this.threadPool.isShutdown())
				this.threadPool.shutdownNow();
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
	}

}
