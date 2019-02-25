package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.CollectionDefinition;
import org.monet.metamodel.Definition;
import org.monet.metamodel.internal.Ref;

public class CollectionPageRender extends NodePageRender {

	public CollectionPageRender() {
		super();
	}

	@Override
	protected void init() {
		loadCanvas("page.node.collection");
		super.init();
	}

	@Override
	protected void initControlInfo() {
		CollectionDefinition definition = (CollectionDefinition) this.node.getDefinition();
		String addList = "";
		int pos = 0;

		super.initControlInfo();

		if (allowEdition(node)) {
			for (Ref add : collectionDefinitionAdds(this.definition)) {
				String nameNode = add.getValue();
				if (pos != 0) addList += ";";
				for (Definition addDefinition : this.dictionary.getAllImplementersOfNodeDefinition(nameNode)) {

					if (addDefinition.isDisabled())
						continue;

					addList += addDefinition.getCode();
				}
				pos++;
			}
		}

		addMark("addList", addList);
		addMark("children", "");
	}

}
