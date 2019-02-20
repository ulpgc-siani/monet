package org.monet.space.kernel.agents;

import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.model.MonetEvent;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.PushClient;
import org.monet.space.kernel.utils.cache.Cache;
import org.monet.space.kernel.utils.cache.Cache.CacheEventListener;
import org.monet.space.kernel.utils.cache.Cache.CacheEventType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class AgentRuleManager implements CacheEventListener<String, Node> {

	private static AgentRuleManager oInstance;

	//
	private HashMap<String, List<String>> nodeWatchTo = new HashMap<String, List<String>>();
	//Dado un nodo, quienes quieren escuchar sus cambios y están cargados
	private HashMap<String, ArrayList<String>> watchersOfNode = new HashMap<String, ArrayList<String>>();
	//Dado un nodo, quienes quieren escuchar sus cambios, pero no pueden pq el nodo no está cargado
	private HashMap<String, ArrayList<String>> vacantWatchersOfNode = new HashMap<String, ArrayList<String>>();

	protected AgentRuleManager() {
	}

	public synchronized static AgentRuleManager getInstance() {
		if (oInstance == null) {
			oInstance = new AgentRuleManager();
		}
		return oInstance;
	}

	@Override
	public void onCacheEvent(Cache<String, Node> sender, Node node, CacheEventType type) {
		AgentLogger agentLogger = AgentLogger.getInstance();
		switch (type) {
			case ADDED:
				agentLogger.debug(" + Loaded node %s", node.getId());
				List<String> nodeIds = node.getNodesToListenForChanges();
				for (String id : nodeIds)
					agentLogger.debug("    * dependant: %s", id);

				this.subscribeNode(node, nodeIds);
				break;
			case REMOVED:
				agentLogger.debug(" - Unloaded node %s", node.getId());

				String viewId = PushClient.generateViewId(node);
				if (AgentPushService.getInstance().isActive(viewId)) {
					try {
						sender.put(node.getId(), node);
					} catch (Exception e) {
						agentLogger.error(e);
					}
				} else {
					this.unsubscribeNode(node);
				}

				break;
		}
	}

	private void subscribeNode(Node node, List<String> nodesToListenIds) {
		synchronized (nodeWatchTo) {
			nodeWatchTo.put(node.getId(), nodesToListenIds);
			watchersOfNode.put(node.getId(), new ArrayList<String>());

			for (String id : nodesToListenIds) {
				ArrayList<String> watchers = watchersOfNode.get(id);
				if (watchers != null) {
					watchers.add(node.getId());
				} else {
					ArrayList<String> watchersOfNotLoaded = vacantWatchersOfNode.get(id);
					if (watchersOfNotLoaded == null) {
						watchersOfNotLoaded = new ArrayList<String>();
						vacantWatchersOfNode.put(id, watchersOfNotLoaded);
					}
					watchersOfNotLoaded.add(node.getId());
				}
			}

			Node mainNode = node.getMainNode();
			if (mainNode != null && !watchersOfNode.get(node.getId()).contains(mainNode.getId())) {
				watchersOfNode.get(node.getId()).add(mainNode.getId());
			}

			if (vacantWatchersOfNode.containsKey(node.getId())) {
				watchersOfNode.put(node.getId(), vacantWatchersOfNode.remove(node.getId()));
			}
		}
	}

	private void unsubscribeNode(Node node) {
		synchronized (nodeWatchTo) {
			List<String> nodeIds = nodeWatchTo.remove(node.getId());
			if (nodeIds != null) {
				for (String id : nodeIds) {
					List<String> watchers = watchersOfNode.get(id);
					if (watchers != null) {
						watchers.remove(node.getId());
					} else {
						watchers = vacantWatchersOfNode.get(id);
						watchers.remove(node.getId());
					}
				}
			}

			ArrayList<String> watcherIds = watchersOfNode.remove(node.getId());
			if (watcherIds != null)
				vacantWatchersOfNode.put(node.getId(), watcherIds);
		}
	}

	public void onNodeChanged(Node node) {
		synchronized (nodeWatchTo) {
			AgentNotifier agentNotifier = AgentNotifier.getInstance();
			NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
			String id = node.getId();
			List<String> watchers = watchersOfNode.get(id);
			if (watchers != null) {
				HashSet<String> activeViews = AgentPushService.getInstance().getActiveViews();
				for (String watcher : watchers) {
					String viewId = "node/" + watcher;
					if (activeViews.contains(viewId))
						agentNotifier.notify(new MonetEvent(MonetEvent.NODE_REFRESH_STATE, null, nodeLayer.loadNode(watcher)));
				}
			}
		}
	}

}
