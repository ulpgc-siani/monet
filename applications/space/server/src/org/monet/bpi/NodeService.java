package org.monet.bpi;

public abstract class NodeService {

	protected static NodeService instance;

	public static Node locate(String name) {
		return instance.locateImpl(name);
	}

	public static Node get(String nodeId) {
		return instance.getImpl(nodeId);
	}

	public static String getKmlLayer(String nodeId) {
		return instance.getKmlLayerImpl(nodeId);
	}

	public static Node create(Class<? extends Node> nodeClass, Node parent) {
		return instance.createImpl(nodeClass, parent);
	}

	public static Node create(String name, Node parent) {
		return instance.createImpl(name, parent);
	}

	protected abstract Node locateImpl(String name);

	protected abstract Node getImpl(String nodeId);

	protected abstract String getKmlLayerImpl(String nodeId);

	protected abstract Node createImpl(Class<? extends Node> nodeClass, Node parent);

	protected abstract Node createImpl(String name, Node parent);

}