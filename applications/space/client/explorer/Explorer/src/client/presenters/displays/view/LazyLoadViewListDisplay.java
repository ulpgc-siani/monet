package client.presenters.displays.view;

import client.core.model.Entity;
import client.core.model.NodeView;
import client.core.model.View;
import client.core.model.ViewList;
import client.presenters.operations.ShowOperation;

import java.util.Map;

public class LazyLoadViewListDisplay<T extends Entity> extends ViewListDisplay<T> {
	private final Map<NodeView, ShowOperation> operationsMap;

	public LazyLoadViewListDisplay(T entity, ViewList<NodeView> nodeViewList, Map<NodeView, ShowOperation> operations) {
		super(entity, nodeViewList);
		this.operationsMap = operations;
	}

	@Override
	public void activateView(ViewDisplay viewDisplay) {
		super.activateView(viewDisplay);

		ShowOperation operation = this.operationsMap.get(viewDisplay.getView());
		operation.inject(services);
		operation.execute();
	}

	@Override
	protected void addViews() {
		for (View view : viewList)
			this.addChild(new LazyLoadViewDisplay(getEntity(), view));
	}

}
