package client.core.model.definition.entity.field;

import client.core.model.Instance;
import client.core.model.List;
import client.core.model.definition.entity.FieldDefinition;
import client.core.model.definition.entity.MultipleableFieldDefinition;

public interface CompositeFieldDefinition extends MultipleableFieldDefinition {

	Instance.ClassName CLASS_NAME = new Instance.ClassName("CompositeFieldDefinition");

	boolean isExtensible();
	boolean isConditional();

	List<FieldDefinition> getFields();
	FieldDefinition getField(String key);
	ViewDefinition getView();

	interface ViewDefinition {
		Summary getSummary();
		Show getShow();

		interface Summary {
			List<String> getFields();
		}

		interface Show {
			List<String> getFields();
			String getLayout();
			String getLayoutExtended();
		}
	}
}
