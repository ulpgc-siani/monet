package client.core.system.workmap;

import client.core.model.Node;
import client.core.model.Role;
import client.core.model.definition.entity.PlaceDefinition.DelegationActionDefinition;
import cosmos.types.Date;

public class DelegationAction extends Action<DelegationActionDefinition> implements client.core.model.workmap.DelegationAction {
	private Step step;
	private Role role;
	private Node orderNode;
	private Date failureDate;

	public DelegationAction() {
		super();
	}

	public DelegationAction(Step step, Date failureDate, Role role, Node orderNode, DelegationActionDefinition definition) {
		super(definition);
		this.step = step;
		this.failureDate = failureDate;
		this.role = role;
		this.orderNode = orderNode;
	}

	@Override
	public Date getFailureDate() {
		return failureDate;
	}

	public void setFailureDate(Date failureDate) {
		this.failureDate = failureDate;
	}

	@Override
	public Step getStep() {
		return step;
	}

	public void setStep(Step step) {
		this.step = step;
	}

	@Override
	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public Node getOrderNode() {
		return this.orderNode;
	}

	public void setOrderNode(Node orderNode) {
		this.orderNode = orderNode;
	}

	@Override
	public ClassName getClassName() {
		return client.core.model.workmap.DelegationAction.CLASS_NAME;
	}
}