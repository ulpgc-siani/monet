package client.services.http.builders.definition.entity.field;

import client.core.model.List;
import client.core.system.MonetList;
import client.core.system.definition.entity.field.SelectFieldDefinition;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import client.services.http.builders.Builder;

import static client.core.model.definition.entity.field.SelectFieldDefinition.SelectDefinition.Flatten;

public class SelectFieldDefinitionBuilder extends FieldDefinitionBuilder<client.core.model.definition.entity.field.SelectFieldDefinition> {

	@Override
	public client.core.model.definition.entity.field.SelectFieldDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		SelectFieldDefinition definition = new SelectFieldDefinition();
		initialize(definition, instance);
		return definition;
	}

	@Override
	public void initialize(client.core.model.definition.entity.field.SelectFieldDefinition object, HttpInstance instance) {
		super.initialize(object, instance);

		SelectFieldDefinition definition = (SelectFieldDefinition)object;
		definition.setTerms(new TermDefinitionBuilder().buildList(instance.getList("terms")));

		if (!instance.getString("source").isEmpty())
			definition.setSource(instance.getString("source"));

		definition.setAllowHistory(getAllowHistory(instance.getObject("allowHistory")));
		definition.setAllowOther(instance.getBoolean("allowOther"));
		definition.setAllowKey(instance.getBoolean("allowKey"));
		definition.setAllowSearch(instance.getBoolean("allowSearch"));
		definition.setSelect(getSelect(instance.getObject("select")));
	}

	private client.core.model.definition.entity.field.SelectFieldDefinition.AllowHistoryDefinition getAllowHistory(HttpInstance instance) {
		if (instance == null)
			return null;

		SelectFieldDefinition.AllowHistoryDefinition allowHistory = new SelectFieldDefinition.AllowHistoryDefinition();
		allowHistory.setDataStore(instance.getString("dataStore"));

		return allowHistory;
	}

	private client.core.model.definition.entity.field.SelectFieldDefinition.SelectDefinition getSelect(HttpInstance instance) {
		if (instance == null)
			return null;

		SelectFieldDefinition.SelectDefinition select = new SelectFieldDefinition.SelectDefinition();

		if (!instance.getString("flatten").isEmpty())
			select.setFlatten(Flatten.fromString(instance.getString("flatten")));

		select.setDepth(instance.getInt("depth"));
		select.setContext(instance.getString("context"));
		select.setRoot(getObject(instance, "root"));
		select.setFilter(getFilter(instance.getObject("filter")));
		select.setEmbedded(instance.getBoolean("embedded"));

		return select;
	}

	private client.core.model.definition.entity.field.SelectFieldDefinition.SelectDefinition.FilterDefinition getFilter(HttpInstance instance) {
		SelectFieldDefinition.SelectDefinition.FilterDefinition filter = new SelectFieldDefinition.SelectDefinition.FilterDefinition();
		List<Object> tags = new MonetList<>();

		if (instance != null)
			for (int i = 0; i < instance.getArray("tags").length(); i++)
				tags.add(getArrayObject(instance, "tags", i));

		filter.setTags(tags);

		return filter;
	}

	protected static class TermDefinitionBuilder implements Builder<client.core.model.definition.entity.field.SelectFieldDefinition.TermDefinition, List<client.core.model.definition.entity.field.SelectFieldDefinition.TermDefinition>> {

		@Override
		public client.core.model.definition.entity.field.SelectFieldDefinition.TermDefinition build(HttpInstance instance) {
			if (instance == null)
				return null;

			SelectFieldDefinition.TermDefinition definition = new SelectFieldDefinition.TermDefinition();
			initialize(definition, instance);
			return definition;
		}

		@Override
		public void initialize(client.core.model.definition.entity.field.SelectFieldDefinition.TermDefinition object, HttpInstance instance) {
			SelectFieldDefinition.TermDefinition definition = (SelectFieldDefinition.TermDefinition)object;

			definition.setKey(instance.getString("key"));
			definition.setLabel(instance.getString("label"));
			definition.setCategory(instance.getBoolean("category"));
			definition.setTerms(buildList(instance.getList("terms")));
		}

		@Override
		public List<client.core.model.definition.entity.field.SelectFieldDefinition.TermDefinition> buildList(HttpList instance) {
			List<client.core.model.definition.entity.field.SelectFieldDefinition.TermDefinition> result = new MonetList<>();

			for (int i = 0; i < instance.getItems().length(); i++)
				result.add(build(instance.getItems().get(i)));

			return result;
		}
	}

}
