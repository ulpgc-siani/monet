package client.core.system.definition.entity.field;

import client.core.model.Instance;
import client.core.system.definition.entity.MultipleableFieldDefinition;

public class DateFieldDefinition extends MultipleableFieldDefinition implements client.core.model.definition.entity.field.DateFieldDefinition {
	private Precision precision;
	private Purpose purpose;
	private client.core.model.definition.entity.field.DateFieldDefinition.RangeDefinition range;
	private boolean allowLessPrecision;

	@Override
	public Instance.ClassName getClassName() {
		return client.core.model.definition.entity.field.DateFieldDefinition.CLASS_NAME;
	}

	@Override
	public boolean allowLessPrecision() {
		return allowLessPrecision;
	}

	public void setAllowLessPrecision(boolean allowLessPrecision) {
		this.allowLessPrecision = allowLessPrecision;
	}

	@Override
	public Precision getPrecision() {
		return precision;
	}

	public void setPrecision(Precision precision) {
		this.precision = precision;
	}

	@Override
	public Purpose getPurpose() {
		return purpose;
	}

	public void setPurpose(Purpose purpose) {
		this.purpose = purpose;
	}

	@Override
	public client.core.model.definition.entity.field.DateFieldDefinition.RangeDefinition getRange() {
		return range;
	}

	public void setRange(client.core.model.definition.entity.field.DateFieldDefinition.RangeDefinition range) {
		this.range = range;
	}

	public static class RangeDefinition implements client.core.model.definition.entity.field.DateFieldDefinition.RangeDefinition {
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
