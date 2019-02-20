package org.monet.deployservice_engine.control.commands;

import org.monet.deployservice_engine.control.ExecutionEvent;
import org.monet.deployservice_engine.control.IExecutionEventListener;

public interface IAsynchronousCommand extends ICommand {
	public static final int BUSY = 0;
	public static final int ILDE = 1;
	
	public int  getState();
	public void setState(int state);
	
	public void addExecutionEventListener(IExecutionEventListener listener);
	public void removeExecutionEventListener(IExecutionEventListener listener);
	public void fireExecutionEvent(ExecutionEvent event);

}
