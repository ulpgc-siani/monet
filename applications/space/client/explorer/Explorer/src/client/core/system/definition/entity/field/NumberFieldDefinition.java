package client.core.system.definition.entity.field;

import client.core.model.Instance;
import client.core.system.definition.entity.MultipleableFieldDefinition;

public class NumberFieldDefinition extends MultipleableFieldDefinition implements client.core.model.definition.entity.field.NumberFieldDefinition {
	private String format;
	private client.core.model.definition.entity.field.NumberFieldDefinition.RangeDefinition range;
	private Edition edition;

	@Override
	public Instance.ClassName getClassName() {
		return client.core.model.definition.entity.field.NumberFieldDefinition.CLASS_NAME;
	}

	@Override
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@Override
	public client.core.model.definition.entity.field.NumberFieldDefinition.RangeDefinition getRange() {
		return range;
	}

	public void setRange(client.core.model.definition.entity.field.NumberFieldDefinition.RangeDefinition range) {
		this.range = range;
	}

	@Override
	public Edition getEdition() {
		return edition;
	}

	public void setEdition(Edition edition) {
		this.edition = edition;
	}

	public static class RangeDefinition implements client.core.model.definition.entity.field.NumberFieldDefinition.RangeDefinition {
		private long min;
		private long max;

		@Override
		public long getMin() {
			return min;
		}

		public void setMin(long min) {
			this.min = min;
		}

		@Override
		public long getMax() {
			return max;
		}

		public void setMax(long max) {
			this.max = max;
		}
	}

}
