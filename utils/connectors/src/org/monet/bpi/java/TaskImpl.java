package org.monet.bpi.java;

import org.monet.api.space.backservice.BackserviceApi;
import org.monet.api.space.backservice.impl.model.workmap.Process;
import org.monet.bpi.*;
import org.monet.metamodel.Definition;
import org.monet.metamodel.internal.Lock;
import org.monet.v3.BPIClassLocator;
import org.monet.v3.model.Dictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class TaskImpl implements Task, BehaviorTask {
	org.monet.api.space.backservice.impl.model.Task task;
	private BPIClassLocator bpiClassLocator;
	private BackserviceApi api;
	private Dictionary dictionary;

	public void injectTask(org.monet.api.space.backservice.impl.model.Task task) {
		this.task = task;
	}

	public void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	public void injectDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	@Override
	public String getId() {
		return this.task.getId();
	}

	@Override
	public String getCode() {
		return this.task.getCode();
	}

	@Override
	public String getName() {
		return this.task.getName();
	}

	@Override
	public void setLabel(String label) {
		this.task.setLabel(label);
	}

	@Override
	public String getLabel() {
		return this.task.getLabel();
	}

	@Override
	public void setDescription(String description) {
		this.task.setDescription(description);
	}

	@Override
	public Process getProcess() {
		return this.api.getTaskProcess(this.task.getId());
	}

	protected Node genericGetTarget() {
		String targetId = this.task.getTargetId();
		org.monet.api.space.backservice.impl.model.Node node = this.api.openNode(targetId);
		Definition definition = this.dictionary.getDefinition(node.getCode());
		if (node == null)
			return null;
		NodeImpl targetNode = bpiClassLocator.instantiateBehaviour(definition);
		targetNode.injectNode(node);
		targetNode.injectApi(this.api);
		targetNode.injectBPIClassLocator(this.bpiClassLocator);
		targetNode.injectDictionary(this.dictionary);
		return targetNode;
	}

	protected void genericSetTarget(Node target) {
		this.task.setTargetId(((NodeImpl) target).node.getId());
	}

	public Map<String, String> getShortCuts() {
		return this.api.getTaskShortCuts(this.task.getId());
	}

	public Node getShortCut(String name) {
		Map<String, String> shortCutsMap = this.api.getTaskShortCuts(this.task.getId());

		org.monet.api.space.backservice.impl.model.Node node = this.api.openNode(shortCutsMap.get(name));
		Definition definition = this.dictionary.getDefinition(node.getCode());

		if (node == null)
			return null;

		NodeImpl shortcutNode = bpiClassLocator.instantiateBehaviour(definition);
		shortcutNode.injectNode(node);
		shortcutNode.injectApi(this.api);
		shortcutNode.injectBPIClassLocator(this.bpiClassLocator);
		shortcutNode.injectDictionary(this.dictionary);
		return shortcutNode;
	}

	protected void setShortCut(String name, Node node) {
		org.monet.api.space.backservice.impl.model.Node monetNode = ((NodeImpl) node).node;
		this.api.addTaskShortCut(this.task.getId(), name, monetNode.getId());
	}

	protected void removeShortCut(String name) {
		this.api.deleteTaskShortCut(this.task.getId(), name);
	}

	@Override
	public Map<String, String> getFlags() {
		return this.api.getTaskFlags(this.task.getId());
	}

	@Override
	public String getFlag(String name) {
		return this.api.getTaskFlags(this.task.getId()).get(name);
	}

	@Override
	public void setFlag(String name, String value) {
		this.api.addTaskFlag(this.task.getId(), name, value);
	}

	@Override
	public void removeFlag(String name) {
		this.api.deleteTaskFlag(this.task.getId(), name);
	}

	public void addLog(String title, String text) {
		this.addLog(title, text, (Iterable<MonetLink>) null);
	}

	public void addLog(String title, String text, Iterable<MonetLink> links) {
		List<org.monet.api.space.backservice.impl.model.MonetLink> linkList = new ArrayList<org.monet.api.space.backservice.impl.model.MonetLink>();
		if (links != null) {
			for (MonetLink link : links)
				linkList.add(((MonetLinkImpl) link).link);
		}
		this.api.addTaskFact(this.task.getId(), title, text, null, linkList.toArray(new org.monet.api.space.backservice.impl.model.MonetLink[0]));
	}

	@Override
	public void save() {
		this.api.saveTask(this.task);
	}

	@Override
	public void resume() {
		this.api.runTask(this.task.getId());
	}

	@Override
	public void unLock(Lock lock) {
		this.api.unLockTask(this.task.getId(), lock.getPlace(), lock.getId());
	}

	@Override
	public void doGoto(String place, String historyText) {
		this.api.gotoPlaceInTask(this.task.getId(), place, historyText);
	}

	public MonetLink toMonetLink() {
		return MonetLink.forTask(this.task.getId(), this.task.getLabel());
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onAbort() {
	}

	@Override
	public void onAssign(User user) {
	}

	@Override
	public void onUnAssign() {
	}

	@Override
	public void onInitialize() {
	}

	@Override
	public void onTerminate() {
	}

}
