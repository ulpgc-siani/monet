package client.core.model.definition.entity;

import client.core.model.List;
import client.core.model.definition.Ref;
import client.core.model.definition.views.IndexViewDefinition;

public interface IndexDefinition extends EntityDefinition {
	ReferenceDefinition getReference();
	List<IndexViewDefinition> getViews();
	IndexViewDefinition getDefaultView();
	IndexViewDefinition getView(String key);

	String DEFAULT = "td";

	interface ReferenceDefinition {
		List<AttributeDefinition> getAttributes();
		AttributeDefinition getAttribute(String key);

		interface AttributeDefinition {
			String getCode();
			String getName();
			String getLabel();
			String getDescription();
			Type getType();
			boolean is(Type type);
			Precision getPrecision();
			Ref getSource();

			enum Type {
				BOOLEAN, STRING, INTEGER, REAL, DATE, TERM, LINK, CHECK, PICTURE, CATEGORY;

				public static Type fromString(String type) {
					return Type.valueOf(type.toUpperCase());
				}
			}

			enum Precision {
				YEARS, MONTHS, DAYS, HOURS, MINUTES, SECONDS;

				public static Precision fromString(String precision) {
					return Precision.valueOf(precision.toUpperCase());
				}
			}
		}
	}

}
