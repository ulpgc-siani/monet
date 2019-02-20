package client.services.http.builders.definition.entity.field;

import client.core.model.definition.entity.field.DateFieldDefinition.Precision;
import client.core.model.definition.entity.field.DateFieldDefinition.Purpose;
import client.core.system.definition.entity.field.DateFieldDefinition;
import client.services.http.HttpInstance;

public class DateFieldDefinitionBuilder extends FieldDefinitionBuilder<client.core.model.definition.entity.field.DateFieldDefinition> {

	@Override
	public client.core.model.definition.entity.field.DateFieldDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		DateFieldDefinition definition = new DateFieldDefinition();
		initialize(definition, instance);
		return definition;
	}

	@Override
	public void initialize(client.core.model.definition.entity.field.DateFieldDefinition object, HttpInstance instance) {
		super.initialize(object, instance);

		DateFieldDefinition definition = (DateFieldDefinition)object;

		if (!instance.getString("precision").isEmpty())
			definition.setPrecision(Precision.fromString(instance.getString("precision")));

		definition.setAllowLessPrecision(instance.getBoolean("allowLessPrecision"));

		if (!instance.getString("purpose").isEmpty())
			definition.setPurpose(Purpose.fromString(instance.getString("purpose")));

		definition.setAllowLessPrecision(instance.getBoolean("allowLessPrecision"));
		definition.setRange(getRange(instance.getObject("range")));
	}

	private client.core.model.definition.entity.field.DateFieldDefinition.RangeDefinition getRange(HttpInstance instance) {
		if (instance == null)
			return null;

		DateFieldDefinition.RangeDefinition range = new DateFieldDefinition.RangeDefinition();
		range.setMin(instance.getLong("min"));
		range.setMax(instance.getLong("max"));

		return range;
	}

}
