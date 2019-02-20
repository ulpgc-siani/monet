package client.core.system.workmap;

import client.core.model.definition.Ref;
import client.core.model.definition.entity.PlaceDefinition.LineActionDefinition;
import cosmos.types.Date;

import java.util.ArrayList;
import java.util.List;

public class LineAction extends Action<LineActionDefinition> implements client.core.model.workmap.LineAction {
	private Date dueDate;

	public LineAction() {
	}

	public LineAction(Date dueDate, LineActionDefinition definition) {
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

	@Override
	public final Stop getDueStop() {
		LineActionDefinition.TimeoutDefinition definition = getTimeoutDefinition();

		if (definition == null)
			return null;

		return definitionToStop(findStop(definition.getTake()));
	}

	@Override
	public final Stop[] getStops() {
		List<Stop> stops = new ArrayList<>();

		for (LineActionDefinition.LineStopDefinition stopDefinition : getDefinition().getStop())
            stops.add(definitionToStop(stopDefinition));

		return stops.toArray(new Stop[stops.size()]);
	}

	private LineActionDefinition.LineStopDefinition findStop(Ref take) {

		for (LineActionDefinition.LineStopDefinition stopDefinition : getDefinition().getStop()) {
			if (stopDefinition.getCode() == take.getDefinition())
				return stopDefinition;
		}

		return null;
	}

	private LineActionDefinition.TimeoutDefinition getTimeoutDefinition() {
		return getDefinition().getTimeout();
	}

	private Stop definitionToStop(final LineActionDefinition.LineStopDefinition stopDefinition) {
		return new Stop() {
			@Override
			public String getCode() {
				return stopDefinition.getCode();
			}

			@Override
			public String getLabel() {
				return stopDefinition.getLabel();
			}
		};
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.workmap.LineAction.CLASS_NAME;
	}

}