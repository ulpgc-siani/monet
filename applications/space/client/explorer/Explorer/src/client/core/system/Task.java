package client.core.system;

import client.core.model.definition.entity.TaskDefinition;
import client.core.model.workmap.Action;
import client.core.model.workmap.WorkMap;
import cosmos.types.Date;

public abstract class Task<Definition extends TaskDefinition> extends Entity<Definition> implements client.core.model.Task<Definition> {
	private Date updateDate;
	private State state;
	private WorkMap workMap;
	private client.core.model.ViewList<client.core.model.TaskView> views;

	public Task() {
	}

	public Task(String id, String label) {
		super(id, label);
	}

	@Override
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public final boolean isActivity() {
		return (this instanceof Activity);
	}

	@Override
	public final boolean isService() {
		return (this instanceof Service);
	}

	@Override
	public final boolean isJob() {
		return (this instanceof Job);
	}

	@Override
	public final String getDefinitionClass() {
		if (isActivity()) return Type.ACTIVITY.toString();
		if (isService()) return Type.SERVICE.toString();
		if (isJob()) return Type.JOB.toString();
		return Type.TASK.toString();
	}

	@Override
	public State getState() {
		return state;
	}

	@Override
	public void setState(State state) {
		this.state = state;
	}

	@Override
	public final boolean isPending() {
		return getState() == State.PENDING;
	}

	@Override
	public final boolean isWaiting() {
		return getState() == State.WAITING;
	}

	@Override
	public final boolean isFailure() {
		return getState() == State.FAILURE;
	}

	@Override
	public WorkMap getWorkMap() {
		return this.workMap;
	}

	public void setWorkMap(WorkMap workMap) {
		this.workMap = workMap;
	}

	@Override
	public final <A extends Action> A getAction() {
		return (A)getWorkMap().getPlace().getAction();
	}

	@Override
	public client.core.model.ViewList<client.core.model.TaskView> getViews() {

		if (views != null)
			return views;

		views = loadViews();

		return views;
	}

	protected abstract client.core.model.ViewList<client.core.model.TaskView> loadViews();

	public void setViews(client.core.model.ViewList<client.core.model.TaskView> views) {
		this.views = views;
	}

	@Override
	public final client.core.model.TaskView getView(client.core.model.Key key) {
		return getViews().get(key);
	}

	@Override
	public final String[] search(String query) {
		return new String[0];
	}

}
