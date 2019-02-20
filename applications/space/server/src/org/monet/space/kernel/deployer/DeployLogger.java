package org.monet.space.kernel.deployer;


public interface DeployLogger {

	public void error(Throwable exception);

	public void error(String message, Object... args);

	public void info(String message, Object... args);

	public void debug(String message, Object... args);

}
