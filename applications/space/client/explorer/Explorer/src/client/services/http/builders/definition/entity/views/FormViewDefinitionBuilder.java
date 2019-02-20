package client.services.http.builders.definition.entity.views;

import client.core.system.definition.views.FormViewDefinition;
import client.services.http.HttpInstance;
import client.services.http.builders.definition.RefBuilder;

public class FormViewDefinitionBuilder extends NodeViewDefinitionBuilder<client.core.model.definition.views.FormViewDefinition> {

	@Override
	public client.core.model.definition.views.FormViewDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		FormViewDefinition viewDefinition = new FormViewDefinition();
		initialize(viewDefinition, instance);
		return viewDefinition;
	}

	@Override
	public void initialize(client.core.model.definition.views.FormViewDefinition object, HttpInstance instance) {
		super.initialize(object, instance);

		FormViewDefinition definition = (FormViewDefinition)object;
		definition.setShow(getShow(instance.getObject("show")));
	}

	private client.core.model.definition.views.FormViewDefinition.ShowDefinition getShow(HttpInstance instance) {
		FormViewDefinition.ShowDefinition show = new FormViewDefinition.ShowDefinition();
		show.setFields(new RefBuilder().buildList(instance.getList("fields")));

		if (!instance.getString("layout").isEmpty())
			show.setLayout(instance.getString("layout"));

		if (!instance.getString("layoutExtended").isEmpty())
			show.setLayoutExtended(instance.getString("layoutExtended"));

		return show;
	}

}