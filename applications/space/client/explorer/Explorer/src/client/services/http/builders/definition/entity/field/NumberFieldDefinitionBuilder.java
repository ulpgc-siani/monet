package client.services.http.builders.definition.entity.field;

import client.core.system.definition.entity.field.NumberFieldDefinition;
import client.services.http.HttpInstance;

import static client.core.model.definition.entity.field.NumberFieldDefinition.Edition;

public class NumberFieldDefinitionBuilder extends FieldDefinitionBuilder<client.core.model.definition.entity.field.NumberFieldDefinition> {

	@Override
	public client.core.model.definition.entity.field.NumberFieldDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		NumberFieldDefinition definition = new NumberFieldDefinition();
		initialize(definition, instance);
		return definition;
	}

	@Override
	public void initialize(client.core.model.definition.entity.field.NumberFieldDefinition object, HttpInstance instance) {
		super.initialize(object, instance);

		NumberFieldDefinition definition = (NumberFieldDefinition)object;
		definition.setFormat(instance.getString("format"));
		definition.setRange(getRange(instance.getObject("range")));

		if (!instance.getString("edition").isEmpty())
			definition.setEdition(Edition.fromString(instance.getString("edition")));
	}

	private client.core.model.definition.entity.field.NumberFieldDefinition.RangeDefinition getRange(HttpInstance instance) {
		if (instance == null)
			return null;

		NumberFieldDefinition.RangeDefinition range = new NumberFieldDefinition.RangeDefinition();
		range.setMin(instance.getInt("min"));
		range.setMax(instance.getInt("max"));

		return range;
	}

}
