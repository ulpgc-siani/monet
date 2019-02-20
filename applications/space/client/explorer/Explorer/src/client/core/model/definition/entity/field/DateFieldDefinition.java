package client.core.model.definition.entity.field;

import client.core.model.Instance;
import client.core.model.definition.entity.MultipleableFieldDefinition;

public interface DateFieldDefinition extends MultipleableFieldDefinition {

	Instance.ClassName CLASS_NAME = new Instance.ClassName("DateFieldDefinition");

	enum Precision {
		YEARS, MONTHS, DAYS, HOURS, MINUTES, SECONDS;

		public static Precision fromString(String precision) {
			return Precision.valueOf(precision.toUpperCase());
		}
	}

	boolean allowLessPrecision();

	enum Purpose {
		NEAR_DATE, DISTANT_DATE;

		public static Purpose fromString(String purpose) {
			return Purpose.valueOf(purpose.toUpperCase());
		}
	}

	Precision getPrecision();
	Purpose getPurpose();
	RangeDefinition getRange();

	interface RangeDefinition {
		long getMin();
		long getMax();
	}

}
