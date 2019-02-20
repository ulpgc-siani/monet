package org.monet.editor;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class MonetLog {
	private static ILog log = null;
	
	public static void init(ILog log) {
		MonetLog.log = log;
	}

	public static void print(String message) {
		print(IStatus.INFO, IStatus.OK, message, null);
	}

	public static void print(Throwable exception) {
		print("Internal Error", exception);
	}

	public static void print(String message, Throwable exception) {
		print(createStatus(IStatus.ERROR, IStatus.ERROR, message, exception));
	}

	public static void print(int severity, int code, String message, Throwable exception) {
		print(createStatus(severity, code, message, exception));
	}

	private static IStatus createStatus(int severity, int code, String message, Throwable exception) {
		return new Status(severity, "MonetEditor", code, message, exception);
	}

	private static void print(IStatus status) {
		if (log == null) return;
		try {
			log.log(status);
		}
		catch (Exception e) {
		}
	}

}
