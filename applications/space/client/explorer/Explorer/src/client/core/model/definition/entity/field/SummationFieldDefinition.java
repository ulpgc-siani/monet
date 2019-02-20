package client.core.model.definition.entity.field;

import client.core.model.Instance;
import client.core.model.List;
import client.core.model.definition.entity.MultipleableFieldDefinition;

public interface SummationFieldDefinition extends MultipleableFieldDefinition {

	Instance.ClassName CLASS_NAME = new Instance.ClassName("SummationFieldDefinition");

	List<SummationItemDefinition> getTerms();
	String getSource();
	SelectDefinition getSelect();
	String getFormat();
	RangeDefinition getRange();

	interface SummationItemDefinition {

		enum Type {
			SIMPLE, INVOICE, ACCOUNT;

			public static Type fromString(String type) {
				return Type.valueOf(type.toUpperCase());
			}
		}

		String getKey();
		String getLabel();
		boolean isMultiple();
		boolean isNegative();
		Type getType();
		List<SummationItemDefinition> getTerms();
	}

	interface SelectDefinition {
		enum Flatten { NONE, ALL, LEVEL, LEAF, INTERNAL;

			public static Flatten fromString(String flatten) {
				return Flatten.valueOf(flatten.toUpperCase());
			}
		}

		Flatten getFlatten();
		int getDepth();
	}

	interface RangeDefinition {
		long getMin();
		long getMax();
	}
}