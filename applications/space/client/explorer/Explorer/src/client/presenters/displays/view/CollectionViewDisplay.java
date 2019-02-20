package client.presenters.displays.view;

import client.core.model.Collection;
import client.core.model.CollectionView;
import client.core.model.List;
import client.core.model.Node;
import client.core.model.definition.Dictionary;
import client.core.model.definition.Ref;
import client.core.model.definition.entity.CollectionDefinition;
import client.core.system.MonetList;

public class CollectionViewDisplay extends SetViewDisplay {

	public static final Type TYPE = new Type("CollectionViewDisplay", SetViewDisplay.TYPE);

	public CollectionViewDisplay(Node node, CollectionView nodeView) {
        super(node, nodeView);
	}

	@Override
	protected void onInjectServices() {
		super.onInjectServices();
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	protected List<String> getEntityAddList() {
		Dictionary dictionary = services.getSpaceService().load().getDictionary();
		List<Ref> result = new MonetList<>();
		Collection<CollectionDefinition> entity = getView().getScope();
		CollectionDefinition definition = entity.getDefinition();

		if (definition != null && definition.getAdd() != null)
			result = definition.getAdd().getNode();

		return dictionary.getDefinitionCodes(result);
	}

	public static class Builder extends ViewDisplay.Builder<Node, CollectionView> {

		protected static void register() {
			registerBuilder(Collection.CLASS_NAME.toString() + CollectionView.CLASS_NAME.toString(), new Builder());
		}

		@Override
		public ViewDisplay build(Node entity, CollectionView view) {
			return new CollectionViewDisplay(entity, view);
		}
	}
}
