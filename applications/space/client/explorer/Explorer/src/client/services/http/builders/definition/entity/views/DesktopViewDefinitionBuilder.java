package client.services.http.builders.definition.entity.views;

import client.core.system.definition.views.DesktopViewDefinition;
import client.services.http.HttpInstance;
import client.services.http.builders.definition.RefBuilder;

public class DesktopViewDefinitionBuilder extends NodeViewDefinitionBuilder<client.core.model.definition.views.DesktopViewDefinition> {

	@Override
	public client.core.model.definition.views.DesktopViewDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		DesktopViewDefinition viewDefinition = new DesktopViewDefinition();
		initialize(viewDefinition, instance);
		return viewDefinition;
	}

	@Override
	public void initialize(client.core.model.definition.views.DesktopViewDefinition object, HttpInstance instance) {
		super.initialize(object, instance);

		DesktopViewDefinition definition = (DesktopViewDefinition)object;
		definition.setShow(getShow(instance.getObject("show")));
	}

	private client.core.model.definition.views.DesktopViewDefinition.ShowDefinition getShow(HttpInstance instance) {
		DesktopViewDefinition.ShowDefinition show = new DesktopViewDefinition.ShowDefinition();
		show.setLinks(new RefBuilder().buildList(instance.getList("links")));
		return show;
	}

}