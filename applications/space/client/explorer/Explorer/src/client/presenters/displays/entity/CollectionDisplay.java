package client.presenters.displays.entity;

import client.core.model.Collection;
import client.core.model.CollectionView;

public class CollectionDisplay extends NodeDisplay<Collection, CollectionView> {

	public static final Type TYPE = new Type("CollectionDisplay", NodeDisplay.TYPE);

	public CollectionDisplay(Collection collection, CollectionView view) {
		super(collection, view);
	}

	@Override
	protected void onInjectServices() {
	}

	@Override
	public Type getType() {
		return TYPE;
	}

}