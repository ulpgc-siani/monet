package client.services.http.builders.definition.entity.field;

import client.core.model.List;
import client.core.model.definition.entity.field.CheckFieldDefinition.SelectDefinition.Flatten;
import client.core.system.MonetList;
import client.core.system.definition.entity.field.CheckFieldDefinition;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import client.services.http.builders.Builder;

public class CheckFieldDefinitionBuilder extends FieldDefinitionBuilder<client.core.model.definition.entity.field.CheckFieldDefinition> {

	@Override
	public client.core.model.definition.entity.field.CheckFieldDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		CheckFieldDefinition definition = new CheckFieldDefinition();
		initialize(definition, instance);
		return definition;
	}

	@Override
	public void initialize(client.core.model.definition.entity.field.CheckFieldDefinition object, HttpInstance instance) {
		super.initialize(object, instance);

		CheckFieldDefinition definition = (CheckFieldDefinition)object;
		definition.setAllowKey(instance.getBoolean("allowKey"));

		if (!instance.getString("source").isEmpty())
			definition.setSource(instance.getString("source"));

		definition.setTerms(new TermDefinitionBuilder().buildList(instance.getList("terms")));
		definition.setSelect(new SelectDefinitionBuilder().build(instance.getObject("select")));
	}

	private static class TermDefinitionBuilder implements Builder<client.core.model.definition.entity.field.CheckFieldDefinition.TermDefinition, List<client.core.model.definition.entity.field.CheckFieldDefinition.TermDefinition>> {

		@Override
		public client.core.model.definition.entity.field.CheckFieldDefinition.TermDefinition build(HttpInstance instance) {
			if (instance == null)
				return null;

			CheckFieldDefinition.TermDefinition definition = new CheckFieldDefinition.TermDefinition();
			initialize(definition, instance);
			return definition;
		}

		@Override
		public void initialize(client.core.model.definition.entity.field.CheckFieldDefinition.TermDefinition object, HttpInstance instance) {
			CheckFieldDefinition.TermDefinition definition = (CheckFieldDefinition.TermDefinition)object;

			definition.setKey(instance.getString("key"));
			definition.setLabel(instance.getString("label"));
			definition.setCategory(instance.getBoolean("category"));
			definition.setTermDefinitions(buildList(instance.getList("terms")));
		}

		@Override
		public List<client.core.model.definition.entity.field.CheckFieldDefinition.TermDefinition> buildList(HttpList instance) {
			List<client.core.model.definition.entity.field.CheckFieldDefinition.TermDefinition> result = new MonetList<>();

			for (int i = 0; i < instance.getItems().length(); i++)
				result.add(build(instance.getItems().get(i)));

			return result;
		}
	}

	private static class SelectDefinitionBuilder implements Builder<client.core.model.definition.entity.field.CheckFieldDefinition.SelectDefinition, List<client.core.model.definition.entity.field.CheckFieldDefinition.SelectDefinition>> {

		@Override
		public client.core.model.definition.entity.field.CheckFieldDefinition.SelectDefinition build(HttpInstance instance) {
			if (instance == null)
				return null;

			CheckFieldDefinition.SelectDefinition definition = new CheckFieldDefinition.SelectDefinition();
			initialize(definition, instance);
			return definition;
		}

		@Override
		public void initialize(client.core.model.definition.entity.field.CheckFieldDefinition.SelectDefinition object, HttpInstance instance) {
			CheckFieldDefinition.SelectDefinition definition = (CheckFieldDefinition.SelectDefinition)object;

			definition.setDepth(instance.getInt("depth"));
			definition.setFilter(getFilter(instance.getObject("filter")));

			if (!instance.getString("flatten").isEmpty())
				definition.setFlatten(Flatten.fromString(instance.getString("flatten")));

			definition.setRoot(getObject(instance, "root"));
		}

		private client.core.model.definition.entity.field.CheckFieldDefinition.SelectDefinition.FilterDefinition getFilter(HttpInstance instance) {
			CheckFieldDefinition.SelectDefinition.FilterDefinition filter = new CheckFieldDefinition.SelectDefinition.FilterDefinition();
			List<Object> tags = new MonetList<>();

			for (int i = 0; i < instance.getArray("tags").length(); i++)
				tags.add(getArrayObject(instance, "tags", i));

			filter.setTags(tags);

			return filter;
		}

		@Override
		public List<client.core.model.definition.entity.field.CheckFieldDefinition.SelectDefinition> buildList(HttpList instance) {
			return new MonetList<>();
		}
	}
}
