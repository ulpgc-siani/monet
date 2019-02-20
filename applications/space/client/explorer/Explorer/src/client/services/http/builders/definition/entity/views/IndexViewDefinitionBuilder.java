package client.services.http.builders.definition.entity.views;

import client.core.system.definition.views.IndexViewDefinition;
import client.services.http.HttpInstance;
import client.services.http.builders.definition.RefBuilder;

public class IndexViewDefinitionBuilder extends ViewDefinitionBuilder<client.core.model.definition.views.IndexViewDefinition> {
	@Override
	public client.core.model.definition.views.IndexViewDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		IndexViewDefinition viewDefinition = new IndexViewDefinition();
		initialize(viewDefinition, instance);
		return viewDefinition;
	}

	@Override
	public void initialize(client.core.model.definition.views.IndexViewDefinition object, HttpInstance instance) {
		super.initialize(object, instance);

		IndexViewDefinition definition = (IndexViewDefinition)object;
		definition.setShow(getShow(instance.getObject("show")));
	}

	private client.core.model.definition.views.IndexViewDefinition.ShowDefinition getShow(HttpInstance instance) {
		IndexViewDefinition.ShowDefinition show = new IndexViewDefinition.ShowDefinition();

		show.setTitle(new RefBuilder().build(instance.getObject("title")));
		show.setLines(new RefBuilder().buildList(instance.getList("lines")));
		show.setLinesBelow(new RefBuilder().buildList(instance.getList("linesBelow")));
		show.setHighlight(new RefBuilder().buildList(instance.getList("highlight")));
		show.setFooter(new RefBuilder().buildList(instance.getList("footer")));
		show.setIcon(new RefBuilder().build(instance.getObject("icon")));
		show.setPicture(new RefBuilder().build(instance.getObject("picture")));

		return show;
	}
}