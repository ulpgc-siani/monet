package client.core.constructors;

import client.core.model.Field;
import client.core.model.Instance;
import client.core.model.definition.entity.FieldDefinition;
import client.core.model.definition.entity.field.*;
import client.core.model.types.Composite;

import java.util.HashMap;
import java.util.Map;

public class CompositeConstructor {

    private static final Map<Instance.ClassName, FieldConstructor> constructors = new HashMap<>();
    static {
        constructors.put(BooleanFieldDefinition.CLASS_NAME, new BooleanFieldConstructor());
        constructors.put(CheckFieldDefinition.CLASS_NAME, new CheckFieldConstructor());
        constructors.put(CompositeFieldDefinition.CLASS_NAME, new CompositeFieldConstructor());
        constructors.put(DateFieldDefinition.CLASS_NAME, new DateFieldConstructor());
        constructors.put(FileFieldDefinition.CLASS_NAME, new FileFieldConstructor());
        constructors.put(LinkFieldDefinition.CLASS_NAME, new LinkFieldConstructor());
        constructors.put(MemoFieldDefinition.CLASS_NAME, new MemoFieldConstructor());
        constructors.put(NodeFieldDefinition.CLASS_NAME, new NodeFieldConstructor());
        constructors.put(NumberFieldDefinition.CLASS_NAME, new NumberFieldConstructor());
        constructors.put(PictureFieldDefinition.CLASS_NAME, new PictureFieldConstructor());
        constructors.put(SelectFieldDefinition.CLASS_NAME, new SelectFieldConstructor());
        constructors.put(SerialFieldDefinition.CLASS_NAME, new SerialFieldConstructor());
        constructors.put(SummationFieldDefinition.CLASS_NAME, new SummationFieldConstructor());
        constructors.put(TextFieldDefinition.CLASS_NAME, new TextFieldConstructor());
        constructors.put(UriFieldDefinition.CLASS_NAME, new UriFieldConstructor());
    }

    public static Composite construct(CompositeFieldDefinition definition) {
        Composite composite = new client.core.system.types.Composite();
        for (FieldDefinition fieldDefinition : definition.getFields())
            composite.add(constructField(fieldDefinition));
        return composite;
    }

    private static Field constructField(FieldDefinition definition) {
        return constructors.get(definition.getClassName()).construct(definition);
    }
}
