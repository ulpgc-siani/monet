package org.monet.space.kernel.model;

import org.monet.space.kernel.agents.AgentPushServiceNotifier;

import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PushClient {

	private AsyncContext context;
	private boolean isCompatibilityMode;
	private String id;
	private String key;
	private String userId;
	private String sessionId;
	private String viewId;
	private Map<String, String> viewContext;
	private ConcurrentLinkedQueue<String> messageQueue;
	private AgentPushServiceNotifier notifier;
	private Long lastConnectionTime;

	public PushClient(String id, String sessionId, String userId) {
		this.isCompatibilityMode = false;
		this.key = PushClient.generateKey(sessionId, id);
		this.userId = userId;
		this.sessionId = sessionId;
		this.id = id;
		this.viewContext = new HashMap<>();
		this.messageQueue = new ConcurrentLinkedQueue<>();
		this.notifier = AgentPushServiceNotifier.getInstance();
		this.lastConnectionTime = null;
	}

	public synchronized void refreshContext(AsyncContext context, boolean compatibilityMode) {
		if (this.context != null) {
			try {
				this.context.complete();
			} catch (Exception ex) {
			}
		}

		this.context = context;
		this.isCompatibilityMode = compatibilityMode;
		this.context.getResponse().setContentType("text/html;charset=UTF-8");

		String message;
		while ((message = messageQueue.poll()) != null) {
			this.push(message);
		}
	}

	public boolean isElapsedTimeExceeded() {
		Long realTime = new Date().getTime();
		return (realTime - this.lastConnectionTime) > 300000;
	}

	public synchronized void push(String message) {
		ServletResponse response = null;
		PrintWriter writer = null;

		try {
			if (this.context != null &&
				(response = this.context.getResponse()) != null &&
				(writer = response.getWriter()) != null) {

				if (isCompatibilityMode) {
					writer.print("<script> parent.goPush(");
					writer.print(message);
					writer.print(");</script>");
					writer.print("<p>                                                                                        </p>");
					writer.print("<p>                                                                                        </p>");
				} else {
					writer.println("/--push--/");
					writer.println(message);
					writer.println("/--end--/");
				}
				writer.flush();
				response.flushBuffer();
				return;
			}
		} catch (Exception e) {
			try {
				this.context.complete();
			} catch (Exception ex) {
				if (this.lastConnectionTime != null && this.isElapsedTimeExceeded()) {
					PushEvent pushEvent = new PushEvent(PushEvent.REMOVE_CLIENT, this);
					this.notifier.notify(pushEvent);
				}
				this.lastConnectionTime = new Date().getTime();
			}
			this.context = null;
		}
		//Channel closed
		this.messageQueue.add(message);
	}

	public void destroy() {
		if (this.context == null) return;

		try {
			this.context.complete();
		} catch (Exception ex) {
		}
	}

	public static Class<?> getObjectClass(String viewId) {
		if (viewId.indexOf("notificationlist") != -1) return NotificationList.class;
		if (viewId.indexOf("node") != -1) return Node.class;
		if (viewId.indexOf("task") != -1) return Task.class;
		if (viewId.indexOf("tasklist") != -1) return TaskList.class;
		if (viewId.indexOf("trash") != -1) return Trash.class;
		return null;
	}

	public static String getObjectId(String viewId) {
		viewId = viewId.replace("notificationlist", "");
		viewId = viewId.replace("node/", "");
		viewId = viewId.replace("task/", "");
		viewId = viewId.replace("tasklist", "");
		viewId = viewId.replace("trash", "");
		return viewId;
	}

	public static String generateKey(String sessionId, String id) {
		return sessionId + id;
	}

	public static String generateViewId(Class<?> clazz, String id) {

		if (clazz == NotificationList.class) return "notificationlist";
		if (clazz == Node.class) return "node/" + id;
		if (clazz == Task.class) return "task/" + id;
		if (clazz == TaskList.class) return "tasklist";
		if (clazz == Trash.class) return "trash";

		return null;
	}

	public static String generateViewId(Object object) {
		BaseObject baseObject;

		if (object instanceof NotificationList) return "notificationlist";

		baseObject = (BaseObject) object;

		if (object instanceof Node) return "node/" + baseObject.getId();
		if (object instanceof Task) return "task/" + baseObject.getId();
		if (object instanceof TaskList) return "tasklist";
		if (object instanceof Trash) return "trash";

		return null;
	}

	public String getKey() {
		return this.key;
	}

	public String getId() {
		return this.id;
	}

	public String getUserId() {
		return this.userId;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public String getViewId() {
		return this.viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

	public Map<String, String> getViewContext() {
		return this.viewContext;
	}

	public void setViewContext(Map<String, String> context) {
		this.viewContext = context;
	}

}
