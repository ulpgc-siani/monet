package client.services.http.builders.definition.entity.field;

import client.core.model.List;
import client.core.system.MonetList;
import client.core.system.definition.entity.field.SummationFieldDefinition;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import client.services.http.builders.Builder;

import static client.core.model.definition.entity.field.SummationFieldDefinition.SelectDefinition.Flatten;
import static client.core.model.definition.entity.field.SummationFieldDefinition.SummationItemDefinition.Type;

public class SummationFieldDefinitionBuilder extends FieldDefinitionBuilder<client.core.model.definition.entity.field.SummationFieldDefinition> {

	@Override
	public client.core.model.definition.entity.field.SummationFieldDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		SummationFieldDefinition definition = new SummationFieldDefinition();
		initialize(definition, instance);
		return definition;
	}

	@Override
	public void initialize(client.core.model.definition.entity.field.SummationFieldDefinition object, HttpInstance instance) {
		super.initialize(object, instance);

		SummationFieldDefinition definition = (SummationFieldDefinition)object;
		definition.setTerms(new TermDefinitionBuilder().buildList(instance.getList("terms")));
		definition.setSource(instance.getString("source"));
		definition.setSelect(getSelect(instance.getObject("select")));
		definition.setFormat(instance.getString("format"));
		definition.setRange(getRange(instance.getObject("range")));
	}

	private client.core.model.definition.entity.field.SummationFieldDefinition.SelectDefinition getSelect(HttpInstance instance) {
		if (instance == null)
			return null;

		SummationFieldDefinition.SelectDefinition select = new SummationFieldDefinition.SelectDefinition();
		select.setDepth(instance.getInt("depth"));

		if (!instance.getString("flatten").isEmpty())
			select.setFlatten(Flatten.fromString(instance.getString("flatten")));

		return select;
	}

	private client.core.model.definition.entity.field.SummationFieldDefinition.RangeDefinition getRange(HttpInstance instance) {
		if (instance == null)
			return null;

		SummationFieldDefinition.RangeDefinition range = new SummationFieldDefinition.RangeDefinition();
		range.setMin(instance.getInt("min"));
		range.setMax(instance.getInt("max"));

		return range;
	}

	private static class TermDefinitionBuilder implements Builder<client.core.model.definition.entity.field.SummationFieldDefinition.SummationItemDefinition, List<client.core.model.definition.entity.field.SummationFieldDefinition.SummationItemDefinition>> {

		@Override
		public client.core.model.definition.entity.field.SummationFieldDefinition.SummationItemDefinition build(HttpInstance instance) {
			if (instance == null)
				return null;

			SummationFieldDefinition.SummationItemDefinition definition = new SummationFieldDefinition.SummationItemDefinition();
			initialize(definition, instance);
			return definition;
		}

		@Override
		public void initialize(client.core.model.definition.entity.field.SummationFieldDefinition.SummationItemDefinition object, HttpInstance instance) {
			client.core.system.definition.entity.field.SummationFieldDefinition.SummationItemDefinition definition = (client.core.system.definition.entity.field.SummationFieldDefinition.SummationItemDefinition)object;

			definition.setKey(instance.getString("key"));
			definition.setLabel(instance.getString("label"));
			definition.setType(Type.fromString(instance.getString("type")));
			definition.setMultiple(instance.getBoolean("multiple"));
			definition.setNegative(instance.getBoolean("negative"));
			definition.setTerms(buildList(instance.getList("terms")));
		}

		@Override
		public List<client.core.model.definition.entity.field.SummationFieldDefinition.SummationItemDefinition> buildList(HttpList instance) {
			List<client.core.model.definition.entity.field.SummationFieldDefinition.SummationItemDefinition> result = new MonetList<>();

			for (int i = 0; i < instance.getItems().length(); i++)
				result.add(build(instance.getItems().get(i)));

			return result;
		}
	}
}
