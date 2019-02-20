package org.monet.docservice.docprocessor.worker.impl;

import com.google.inject.Inject;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.configuration.Configuration;
import org.monet.docservice.docprocessor.data.impl.DatabaseRepository;
import org.monet.docservice.docprocessor.worker.DocumentPreviewsCleaner;

import java.util.Timer;
import java.util.TimerTask;

public class DocumentPreviewsCleanerImpl extends TimerTask implements DocumentPreviewsCleaner {
	private Logger logger;
	private Configuration configuration;
	private DatabaseRepository repository;
	private Timer timer;

	private static final int EVERY_EIGHT_HOURS = 8*60*60;

	@Inject
	public DocumentPreviewsCleanerImpl(Logger logger, Configuration configuration) {
		logger.debug("DocumentPreviewsCleanerImpl(%s)", logger);

		this.logger = logger;
		this.configuration = configuration;
	}

	@Inject
	public void injectRepository(DatabaseRepository repository) {
		this.repository = repository;
	}

	@Override
	public void init() {
		logger.debug("init()");

		this.timer = new Timer("DocumentPreviewsCleaner", true);
		this.timer.schedule(this, 1000, EVERY_EIGHT_HOURS * 1000);
	}

	public void run() {
		logger.debug("Cleaning document previews cache");

		try {
			repository.cleanDocumentPreviews();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		logger.debug("Finishing cleaning document previews cache");
	}

	@Override
	public void destroy() {
		logger.info("Stopping document previews cleaner threads");
		this.timer.cancel();
		this.timer.purge();
	}

}
