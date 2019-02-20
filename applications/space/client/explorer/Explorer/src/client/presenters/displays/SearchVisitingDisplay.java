package client.presenters.displays;

import client.presenters.operations.Operation;
import client.presenters.operations.ShowHomeOperation;

public class SearchVisitingDisplay extends VisitingDisplay<SearchIndexDisplay> {
	private final String condition;

	public static final Type TYPE = new Type("SearchVisitingDisplay", VisitingDisplay.TYPE);

	public SearchVisitingDisplay(SearchIndexDisplay display, String condition, cosmos.presenters.Operation from) {
		super(display, from);
		this.condition = condition;
	}

	@Override
	public Operation toOperation() {
		return null;
	}

	@Override
	protected void onInjectServices() {
	}

	@Override
	public String getLabel() {
		return services.getTranslatorService().translateSearchForCondition(condition);
	}

	@Override
    public void back() {

		if (from == null) {
			ShowHomeOperation operation = new ShowHomeOperation(getOperationContext());
			operation.inject(services);
			operation.execute();
			return;
		}

		from.execute();
	}

	@Override
	public Type getType() {
		return TYPE;
	}
}
