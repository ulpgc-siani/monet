package client.core.system.workmap;

import client.core.model.definition.entity.PlaceDefinition.WaitActionDefinition;
import cosmos.types.Date;

public class WaitAction extends Action<WaitActionDefinition> implements client.core.model.workmap.WaitAction {
	private Date dueDate;

	public WaitAction() {
	}

	public WaitAction(Date dueDate, WaitActionDefinition definition) {
		super(definition);
		this.dueDate = dueDate;
	}

	@Override
	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public final Step getStep() {
		return getDueDate()!=null? Step.RE_CONFIGURE: Step.CONFIGURE;
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.workmap.WaitAction.CLASS_NAME;
	}

}