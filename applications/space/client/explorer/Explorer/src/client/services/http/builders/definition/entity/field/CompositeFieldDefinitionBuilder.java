package client.services.http.builders.definition.entity.field;

import client.core.model.List;
import client.core.system.MonetList;
import client.core.system.definition.entity.field.CompositeFieldDefinition;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import client.services.http.builders.Builder;

public class CompositeFieldDefinitionBuilder extends FieldDefinitionBuilder<client.core.model.definition.entity.field.CompositeFieldDefinition> {

	@Override
	public client.core.model.definition.entity.field.CompositeFieldDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		CompositeFieldDefinition definition = new CompositeFieldDefinition();
		initialize(definition, instance);
		return definition;
	}

	@Override
	public void initialize(client.core.model.definition.entity.field.CompositeFieldDefinition object, HttpInstance instance) {
		super.initialize(object, instance);

		CompositeFieldDefinition definition = (CompositeFieldDefinition)object;
		definition.setExtensible(instance.getBoolean("extensible"));
		definition.setConditional(instance.getBoolean("conditional"));
		definition.setFields(new FieldDefinitionBuilder<>().buildList(instance.getList("fields")));
		definition.setView(new ViewDefinitionBuilder().build(instance.getObject("view")));
	}

	private static class ViewDefinitionBuilder implements Builder<client.core.model.definition.entity.field.CompositeFieldDefinition.ViewDefinition, List<client.core.model.definition.entity.field.CompositeFieldDefinition.ViewDefinition>> {

		@Override
		public client.core.model.definition.entity.field.CompositeFieldDefinition.ViewDefinition build(HttpInstance instance) {
			if (instance == null)
				return null;

			CompositeFieldDefinition.ViewDefinition definition = new CompositeFieldDefinition.ViewDefinition();
			initialize(definition, instance);
			return definition;
		}

		@Override
		public void initialize(client.core.model.definition.entity.field.CompositeFieldDefinition.ViewDefinition object, HttpInstance instance) {
			CompositeFieldDefinition.ViewDefinition definition = (CompositeFieldDefinition.ViewDefinition)object;

			definition.setShow(getShow(instance.getObject("show")));
			definition.setSummary(getSummary(instance.getObject("summary")));
		}

		@Override
		public List<client.core.model.definition.entity.field.CompositeFieldDefinition.ViewDefinition> buildList(HttpList instance) {
			return new MonetList<>();
		}

		private client.core.model.definition.entity.field.CompositeFieldDefinition.ViewDefinition.Show getShow(HttpInstance instance) {

			if (instance == null)
				return null;

			CompositeFieldDefinition.ViewDefinition.Show show = new CompositeFieldDefinition.ViewDefinition.Show();

			if (!instance.getString("layout").isEmpty())
				show.setLayout(instance.getString("layout"));

			if (!instance.getString("layoutExtended").isEmpty())
				show.setLayoutExtended(instance.getString("layoutExtended"));

			List<String> fields = new MonetList<>();
			for (int i = 0; i < instance.getArray("fields").length(); i++)
				fields.add((String) getArrayObject(instance, "fields", i));

			show.setFields(fields);

			return show;
		}

		private client.core.model.definition.entity.field.CompositeFieldDefinition.ViewDefinition.Summary getSummary(HttpInstance instance) {

			if (instance == null)
				return null;

			CompositeFieldDefinition.ViewDefinition.Summary summary = new CompositeFieldDefinition.ViewDefinition.Summary();
			summary.setFields(HttpInstance.toList(instance.getArray("fields")));

			return summary;
		}
	}

}
