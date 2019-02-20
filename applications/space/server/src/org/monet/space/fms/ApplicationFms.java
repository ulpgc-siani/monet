package org.monet.space.fms;

import org.monet.space.fms.configuration.Configuration;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.applications.Application;

public class ApplicationFms extends Application {
	public static final String NAME = "fms";

	private ApplicationFms() {
		super();
	}

	public synchronized static Application getInstance() {
		if (instance == null) instance = new ApplicationFms();
		return instance;
	}

	public static Configuration getConfiguration() {
		return Configuration.getInstance();
	}

	public static String getCallbackUrl() {
		return getConfiguration().getUrl();
	}

	@Override
	public void run() {
		this.isRunning = true;
	}

	@Override
	public void stop() {
		this.isRunning = false;
	}

	public void clean() {
		String tempDir = Configuration.getInstance().getTempDir();
		AgentFilesystem.removeDir(tempDir);
	}

}
