package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.TaskDefinition;
import org.monet.space.kernel.library.LibraryString;

import java.util.HashMap;

public class TaskDefinitionRender extends OfficeRender {
	protected TaskDefinition definition;

	public TaskDefinitionRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
		this.definition = (TaskDefinition) target;
	}

	@Override
	protected void init() {
		loadCanvas("task");

		this.initProperties();
		this.initBehaviour();
	}

	protected void initProperties() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("code", this.definition.getCode());
		map.put("label", LibraryString.cleanSpecialChars(this.definition.getLabelString()));
		map.put("description", LibraryString.cleanSpecialChars(this.definition.getDescriptionString()));

		addMark("properties", block("properties", map));
	}

	protected void initBehaviour() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		addMark("behaviour", block("behaviour", map));
	}

}
