package client.core.system.definition.entity.field;

import client.core.model.Instance;
import client.core.system.definition.entity.MultipleableFieldDefinition;

public class SerialFieldDefinition extends MultipleableFieldDefinition implements client.core.model.definition.entity.field.SerialFieldDefinition {
	private client.core.model.definition.entity.field.SerialFieldDefinition.SerialDefinition serial;

	@Override
	public Instance.ClassName getClassName() {
		return client.core.model.definition.entity.field.SerialFieldDefinition.CLASS_NAME;
	}

	@Override
	public client.core.model.definition.entity.field.SerialFieldDefinition.SerialDefinition getSerial() {
		return serial;
	}

	public void setSerial(client.core.model.definition.entity.field.SerialFieldDefinition.SerialDefinition serial) {
		this.serial = serial;
	}

	public static class SerialDefinition implements client.core.model.definition.entity.field.SerialFieldDefinition.SerialDefinition {
		private String format;
		private String name;

		@Override
		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		@Override
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}