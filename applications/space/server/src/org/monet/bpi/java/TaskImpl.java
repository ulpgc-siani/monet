package org.monet.bpi.java;

import org.monet.bpi.*;
import org.monet.metamodel.internal.Lock;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.HistoryStoreLayer;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.machines.ttm.behavior.ProcessBehavior;
import org.monet.space.kernel.model.TaskFact;

import java.util.Date;

public abstract class TaskImpl implements Task, BehaviorTask {

	BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();
	TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
	HistoryStoreLayer historyStoreLayer = ComponentPersistence.getInstance().getHistoryStoreLayer();
	org.monet.space.kernel.model.Task task;

	void injectTask(org.monet.space.kernel.model.Task task) {
		this.task = task;
	}

	@Override
	public String getId() {
		return this.task.getId();
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
	public void setUrgent(boolean value) {
		this.task.setUrgent(value);
	}

	@Override
	public boolean isUrgent() {
		return this.task.isUrgent();
	}

	@Override
	public void setComments(String comments) {
		this.task.setComments(comments);
	}

	@Override
	public String getComments() {
		return this.task.getComments();
	}

	protected Node genericGetTarget() {
		org.monet.space.kernel.model.Node node = this.task.getTarget();
		if (node == null)
			return null;
		NodeImpl targetNode = bpiClassLocator.instantiateBehaviour(node.getDefinition());
		targetNode.injectNode(node);
		return targetNode;
	}

	protected void genericSetTarget(Node target) {
		this.task.setTarget(((NodeImpl) target).node);
	}

	protected Node getShortCut(String name) {
		org.monet.space.kernel.model.Node node = this.task.getShortcutInstance(name);
		if (node == null)
			return null;
		NodeImpl shorcutNode = bpiClassLocator.instantiateBehaviour(node.getDefinition());
		shorcutNode.injectNode(node);
		return shorcutNode;
	}

	protected void setShortCut(String name, Node node) {
		this.task.addShortcutInstance(name, ((NodeImpl) node).node);
	}

	protected void removeShortCut(String name) {
		this.task.removeShortcutInstance(name);
	}

	@Override
	public String getFlag(String name) {
		return this.task.getFlag(name);
	}

	@Override
	public boolean isFlagActive(String name) {
		return this.task.isFlagActive(name);
	}

	@Override
	public void setFlag(String name, String value) {
		this.task.setFlag(name, value);
	}

	@Override
	public void setFlag(String name, boolean value) {
		this.task.setFlag(name, String.valueOf(value));
	}

	@Override
	public void removeFlag(String name) {
		this.task.removeFlag(name);
	}

	@Override
	public void doGoto(String place, String historyText) {
		this._goto(place, historyText);
	}

	@Override
	public String currentPlace() {
		ProcessBehavior process = this.task.getProcess();
		return process.getCurrentPlace().getName();
	}

	public void addLog(String title, String text) {
		this.addLog(title, text, (Iterable<MonetLink>) null);
	}

	public void addLog(String title, String text, Iterable<MonetLink> links) {
		TaskFact fact = new TaskFact();
		fact.setCreateDate(new Date());
		fact.setTaskId(this.task.getId());
		fact.setTitle(title);
		fact.setSubTitle(text);
		if (links != null) {
			for (MonetLink link : links)
				fact.addLink(((MonetLinkImpl) link).link);
		}
		this.task.getProcess().addFact(fact);
	}

	@Override
	public void save() {
		this.taskLayer.saveTask(this.task);
	}

	@Override
	public void resume() {
		this.taskLayer.runTask(this.task);
	}

	protected void lock(Lock lock) {
		ProcessBehavior process = this.task.getProcess();
		process.lock(lock);
		process.resume();
	}

	protected void unlock(Lock lock) {

		if (this.task.isFinished())
			return;

		ProcessBehavior process = this.task.getProcess();
		process.unlock(lock);
		process.resume();
	}

	protected void _goto(String placeName, String historyText) {
		ProcessBehavior process = this.task.getProcess();
		process.doGoto(placeName, historyText);
		process.resume();
	}

	public User getOwner() {
		org.monet.space.kernel.model.User owner = this.task.getOwner();
		if (owner != null) return null;

		UserImpl bpiOwner = new UserImpl();
		bpiOwner.injectUser(owner);

		return bpiOwner;
	}

	public void assignTo(User user, String reason) {
		this.taskLayer.saveTaskOwner(this.task, ((UserImpl)user).user, reason);
	}

	public void free() {
		this.taskLayer.saveTaskOwner(this.task, null, "");
	}

	public void abort() {
		this.taskLayer.abortTask(this.task.getId());
	}

	public MonetLink toMonetLink() {
		return MonetLink.forTask(this.task.getId(), this.task.getLabel());
	}

	public boolean isFinished() {
		return this.task.isFinished();
	}

	public boolean isAborted() {
		return this.task.isFinished();
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
	public void onUnassign() {
	}

	@Override
	public void onInitialize() {
	}

	@Override
	public void onTerminate() {
	}

}
