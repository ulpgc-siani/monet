package org.monet.bpi.java;

import org.monet.api.backservice.BackserviceApi;
import org.monet.api.backservice.impl.model.Node;
import org.monet.api.backservice.impl.model.Task;
import org.monet.api.backservice.impl.model.TaskFact;
import org.monet.bpi.BPIBaseNode;
import org.monet.bpi.BPIBehaviorTask;
import org.monet.bpi.BPICube;
import org.monet.bpi.BPITask;
import org.monet.bpi.types.Link;
import org.monet.v2.BPIClassLocator;
import org.monet.v2.metamodel.Definition;
import org.monet.v2.model.CustomFactExtraData;
import org.monet.v2.model.Dictionary;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public abstract class BPITaskImpl<Target extends BPIBaseNode<?>, Input extends BPIBaseNode<?>, Output extends BPIBaseNode<?>, WorkPlace extends Enum<?>, WorkLine extends Enum<?>, WorkStop extends Enum<?>, Lock extends Enum<?>, FormLock extends Enum<?>, ServiceUseLock extends Enum<?>> implements BPITask<Target, Input, Output, WorkPlace>, BPIBehaviorTask<WorkPlace, WorkLine, WorkStop, Lock, ServiceUseLock, FormLock> {

	HashMap<String, String> dynNames = new HashMap<String, String>();
	Task task;
	ArrayList<BPICubeImpl<?>> cubes = new ArrayList<BPICubeImpl<?>>();
	private Dictionary dictionary;
	private BPIClassLocator bpiClassLocator;
	private BackserviceApi api;

	void injectTask(Task task) {
		this.task = task;
	}

	protected void setDynamicName(String key, String value) {
		this.dynNames.put(key, value);
	}

	protected String resolve(String key) {
		return this.dynNames.get(key);
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

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Target> T getTarget() {
		String targetId = this.task.getTargetId();

		if (targetId == null || targetId.equals("-1"))
			return null;

		Node node = this.api.openNode(targetId);
		Definition definition = this.dictionary.getDefinition(node.getCode());
		BPIBaseNodeImpl<?> targetNode = bpiClassLocator.getDefinitionInstance(definition.getName());
		targetNode.injectNode(node);
		targetNode.injectApi(this.api);
		targetNode.injectDictionary(this.dictionary);
		targetNode.injectBPIClassLocator(this.bpiClassLocator);
		return (T) targetNode;
	}

	@Override
	public void setTarget(Target target) {
		this.task.setTargetId(((BPIBaseNodeImpl<?>) target).node.getId());
		this.api.saveTask(this.task);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Input> T getInput() {
		String inputId = this.task.getInputId();

		if (inputId == null || inputId.equals("-1"))
			return null;

		Node node = this.api.openNode(inputId);
		Definition definition = this.dictionary.getDefinition(node.getCode());
		BPIBaseNodeImpl<?> inputNode = bpiClassLocator.getDefinitionInstance(definition.getName());
		inputNode.injectNode(node);
		inputNode.injectApi(this.api);
		inputNode.injectDictionary(this.dictionary);
		inputNode.injectBPIClassLocator(this.bpiClassLocator);
		return (T) inputNode;
	}

	@Override
	public void setInput(Input input) {
		this.task.setInputId(((BPIBaseNodeImpl<?>) input).node.getId());
		this.api.saveTask(this.task);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Output> T getOutput() {
		String outputId = this.task.getOutputId();

		if (outputId == null || outputId.equals(-1))
			return null;

		Node node = this.api.openNode(outputId);
		Definition definition = this.dictionary.getDefinition(node.getCode());
		BPIBaseNodeImpl<?> outputNode = bpiClassLocator.getDefinitionInstance(definition.getName());
		outputNode.injectNode(node);
		outputNode.injectApi(this.api);
		outputNode.injectDictionary(this.dictionary);
		outputNode.injectBPIClassLocator(this.bpiClassLocator);
		return (T) outputNode;
	}

	@Override
	public void setOutput(Output output) {
		this.task.setOutputId(((BPIBaseNodeImpl<?>) output).node.getId());
		this.api.saveTask(this.task);
	}

	@Override
	public void keepLock(String id) {
	}

	@Override
	public void throwException(WorkPlace workPlaceCode) {
	}

	@Override
	public String getContextVariable(String name) {
		return this.api.getTaskContextVariables(this.task.getId()).get(name);
	}

	@Override
	public void setContextVariable(String name, String value) {
		this.api.addTaskContextVariable(this.task.getId(), name, value);
	}

	@Override
	public void removeContextVariable(String name) {
		this.api.deleteTaskContextVariable(this.task.getId(), name);
	}

	private void addLog(CustomFactExtraData extraData) {
		TaskFact fact = new TaskFact();
		fact.setCreateDate(new Date());
		fact.setTaskId(this.task.getId());
		fact.setType("none");
		fact.setExtraData(extraData);
		this.api.addTaskFact(this.task.getId(), "none", "", "", "", extraData.getTitle(), extraData.getText());
	}

	public void addLog(String title, String text) {
		CustomFactExtraData extraData = new CustomFactExtraData();
		extraData.setTitle(title);
		extraData.setText(text);
		this.addLog(extraData);
	}

	public void addLog(String title, String text, Link link) {
		CustomFactExtraData extraData = new CustomFactExtraData();
		extraData.setTitle(title);
		extraData.setText(text);
		extraData.setLink(link);
		this.addLog(extraData);
	}

	public void loadLogs() {
		this.api.getTaskFacts(this.task.getId());
	}

	@Override
	public void save() {
		this.api.saveTask(this.task);
	}

	@Override
	public void resume() {
		this.api.runTask(this.task.getId());
	}

	@SuppressWarnings("unchecked")
	protected <T extends BPICube<?>> T getCube(String cubeName) {
		BPICubeImpl<?> cube = bpiClassLocator.getDefinitionInstance(cubeName);
		cube.setDefinition(this.dictionary.getDefinition(cubeName));
		this.cubes.add(cube);
		return (T) cube;
	}

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	public void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}

	public void injectDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}
}
