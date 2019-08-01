package org.monet.bpi;

import org.monet.bpi.java.MonetLinkImpl;
import org.monet.space.kernel.model.MonetLink.Type;

public abstract class MonetLink {

	public static MonetLink forNode(String nodeId, String label) {
		return new MonetLinkImpl(Type.Node, nodeId, label, false);
	}

	public static MonetLink forNode(String nodeId, String label, boolean editMode) {
		return new MonetLinkImpl(Type.Node, nodeId, label, editMode);
	}

	public static MonetLink forTask(String taskId, String label) {
		return new MonetLinkImpl(Type.Task, taskId, label, false);
	}

	public void withView(String view) {
	}

}
