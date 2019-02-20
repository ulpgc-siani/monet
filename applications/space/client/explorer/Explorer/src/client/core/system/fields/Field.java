package client.core.system.fields;

import client.core.model.definition.entity.FieldDefinition;
import client.core.system.Entity;

import java.util.ArrayList;
import java.util.List;

public abstract class Field<Definition extends FieldDefinition, Value> extends Entity<Definition> implements client.core.model.Field<Definition, Value> {

    private static final String ATTRIBUTE_SEPARATOR = "/";
    private static final String MULTIPLE_ATTRIBUTE_INDEX_SEPARATOR = ":";
	private String code;

	public Field(Type type) {
		this(null, null, type);
	}

	public Field(String code, String label, Type type) {
		super(null, label);
		this.code = code;
		this.type = type;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public FieldDefinition.FieldType getFieldType() {
		return getDefinition().getFieldType();
	}

    @Override
    public String getPath() {
        List<String> fieldPaths = new ArrayList<>();
        Field owner = (Field) getOwner();

        fieldPaths.add(getFieldPath(this));
        while (owner != null) {
            if (!owner.isMultiple())
                fieldPaths.add(getFieldPath(owner));

            owner = (Field) owner.getOwner();
        }

        return serialize(fieldPaths);
    }

	@Override
	public boolean equals(Object obj) {
        if (obj instanceof String) return obj.equals(code);
		if (!(obj instanceof Field)) return false;
		if (getValue() == null && ((Field) obj).getValue() != null) return false;
		if (getValue() != null && ((Field) obj).getValue() == null) return false;
		return super.equals(obj) && ((getValue() == null && getValue() == ((Field) obj).getValue()) || getValue().equals(((Field) obj).getValue()));
	}

    private String getFieldPath(Field field) {
        String result = field.getCode();
        client.core.model.Field owner = (Field) field.getOwner();

        if (owner != null && owner.isMultiple())
            result += MULTIPLE_ATTRIBUTE_INDEX_SEPARATOR + ((MultipleField) owner).getPosition(field);

        return result;
    }

    private String serialize(List<String> fieldPaths) {
        String result = "";

        for (int i = fieldPaths.size() - 1; i >= 0; i--)
            result += (result.isEmpty() ? "" : ATTRIBUTE_SEPARATOR) + fieldPaths.get(i);

        return result;
    }
}
