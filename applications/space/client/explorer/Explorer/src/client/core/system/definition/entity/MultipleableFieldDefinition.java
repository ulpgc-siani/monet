package client.core.system.definition.entity;

public class MultipleableFieldDefinition extends FieldDefinition implements client.core.model.definition.entity.MultipleableFieldDefinition {

	private boolean multiple;
    private Boundary boundary;

	@Override
	public boolean isMultiple() {
		return multiple;
	}

    @Override
    public Boundary getBoundary() {
        return boundary;
    }

    public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

    public void setBoundary(Boundary boundary) {
        this.boundary = boundary;
    }
}
