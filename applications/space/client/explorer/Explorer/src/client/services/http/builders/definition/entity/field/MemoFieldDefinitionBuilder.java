package client.services.http.builders.definition.entity.field;

import client.core.system.definition.entity.field.MemoFieldDefinition;
import client.services.http.HttpInstance;
import client.services.http.builders.definition.entity.field.FieldDefinitionBuilder;

import static client.core.model.definition.entity.field.MemoFieldDefinition.EditionDefinition.*;

public class MemoFieldDefinitionBuilder extends FieldDefinitionBuilder<client.core.model.definition.entity.field.MemoFieldDefinition> {

	@Override
	public client.core.model.definition.entity.field.MemoFieldDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		MemoFieldDefinition definition = new MemoFieldDefinition();
		initialize(definition, instance);
		return definition;
	}

	@Override
	public void initialize(client.core.model.definition.entity.field.MemoFieldDefinition object, HttpInstance instance) {
		super.initialize(object, instance);

		MemoFieldDefinition definition = (MemoFieldDefinition)object;
		definition.setAllowHistory(getAllowHistory(instance.getObject("allowHistory")));
		definition.setLength(getLength(instance.getObject("length")));
		definition.setEdition(getEdition(instance.getObject("edition")));
	}

	private client.core.model.definition.entity.field.MemoFieldDefinition.AllowHistoryDefinition getAllowHistory(HttpInstance instance) {
		if (instance == null)
			return null;

		MemoFieldDefinition.AllowHistoryDefinition allowHistory = new MemoFieldDefinition.AllowHistoryDefinition();
		allowHistory.setDataStore(instance.getString("dataStore"));

		return allowHistory;
	}

	private client.core.model.definition.entity.field.MemoFieldDefinition.LengthDefinition getLength(HttpInstance instance) {
		if (instance == null)
			return null;

		MemoFieldDefinition.LengthDefinition length = new MemoFieldDefinition.LengthDefinition();
		length.setMax(instance.getInt("max"));

		return length;
	}

	private client.core.model.definition.entity.field.MemoFieldDefinition.EditionDefinition getEdition(HttpInstance instance) {
		if (instance == null)
			return null;

		MemoFieldDefinition.EditionDefinition edition = new MemoFieldDefinition.EditionDefinition();
		edition.setMode(Mode.fromString(instance.getString("mode")));

		return edition;
	}

}
