package client.services.http.builders.definition.entity.field;

import client.core.system.definition.entity.field.PictureFieldDefinition;
import client.services.http.HttpInstance;

public class PictureFieldDefinitionBuilder extends FieldDefinitionBuilder<client.core.model.definition.entity.field.PictureFieldDefinition> {

	@Override
	public client.core.model.definition.entity.field.PictureFieldDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		PictureFieldDefinition definition = new PictureFieldDefinition();
		initialize(definition, instance);
		return definition;
	}

	@Override
	public void initialize(client.core.model.definition.entity.field.PictureFieldDefinition object, HttpInstance instance) {
		super.initialize(object, instance);

		PictureFieldDefinition definition = (PictureFieldDefinition)object;
		definition.setDefaultValue(instance.getString("defaultValue"));
		definition.setIsProfilePhoto(instance.getBoolean("isProfilePhoto"));
		definition.setSize(getSize(instance.getObject("size")));
        definition.setLimit(instance.getLong("limit"));
	}

	private client.core.model.definition.entity.field.PictureFieldDefinition.SizeDefinition getSize(HttpInstance instance) {
		if (instance == null)
			return null;

		PictureFieldDefinition.SizeDefinition range = new PictureFieldDefinition.SizeDefinition();
		range.setWidth(instance.getInt("width"));
		range.setHeight(instance.getInt("height"));

		return range;
	}

}
