package client.core.model.definition.entity;

public interface MultipleableFieldDefinition extends FieldDefinition {
	boolean isMultiple();
    Boundary getBoundary();

    interface Boundary {
        int getMin();
        int getMax();
    }
}
