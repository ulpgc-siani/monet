package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.FormDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.constants.TaskState;
import org.monet.space.kernel.model.Node;

import java.util.HashMap;

public class ViewRender extends OfficeRender {

	public ViewRender() {
		super();
	}

	@Override
	protected void init() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String codeView = this.getParameterAsString("view");

		loadCanvas("view");

		if (codeView.isEmpty()) {
			addMark("view", block("view.isNull", map));
			return;
		}

		addMark("view", this.initView(codeView));
	}

	protected String initView(String codeView) {
		return "";
	}

	protected String getTaskStateLabel(String state) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String stateLabel = "";

		if (state.equals(TaskState.NEW)) stateLabel = block("taskState.new", map);
		else if (state.equals(TaskState.PENDING)) stateLabel = block("taskState.pending", map);
		else if (state.equals(TaskState.WAITING)) stateLabel = block("taskState.waiting", map);
		else if (state.equals(TaskState.EXPIRED)) stateLabel = block("taskState.expired", map);
		else if (state.equals(TaskState.FINISHED)) stateLabel = block("taskState.finished", map);
		else if (state.equals(TaskState.ABORTED)) stateLabel = block("taskState.aborted", map);
		else if (state.equals(TaskState.FAILURE)) stateLabel = block("taskState.failure", map);
		else stateLabel = block("taskState.undefined", map);

		return stateLabel;
	}

	protected String getFieldSourceFrom(Node node, Object from) {
		String fromParameter = "";

		if (from == null)
			return fromParameter;

		if (from instanceof String)
			fromParameter = (String) from;
		else if (from instanceof Ref) {
			String fieldName = ((Ref) from).getValue();
			fromParameter = node.getFieldValue(fieldName);
		}

		return fromParameter;
	}

	protected String getFieldSourceFromMask(Node node, Object from) {
		String fromParameter = "";

		if (from == null)
			return fromParameter;

		if (from instanceof String)
			fromParameter = (String) from;
		else if (from instanceof Ref) {
			String fieldName = ((Ref) from).getValue();
			fromParameter = "_field:" + ((FormDefinition) node.getDefinition()).getField(fieldName).getCode();
		}

		return fromParameter;
	}

}
