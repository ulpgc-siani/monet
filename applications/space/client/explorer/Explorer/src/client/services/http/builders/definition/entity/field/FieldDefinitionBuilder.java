package client.services.http.builders.definition.entity.field;

import client.core.model.List;
import client.core.system.definition.entity.MultipleableFieldDefinition;
import client.core.system.MonetList;
import client.core.system.definition.entity.FieldDefinition;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import client.services.http.builders.Builder;
import client.services.http.builders.definition.DefinitionBuilder;

import java.util.HashMap;
import java.util.Map;

import static client.core.model.Field.Type;
import static client.core.model.definition.entity.FieldDefinition.Display;
import static client.core.model.definition.entity.FieldDefinition.Template;

public class FieldDefinitionBuilder<T extends client.core.model.definition.entity.FieldDefinition> extends DefinitionBuilder<T> {

	private static final Map<Type, FieldDefinitionBuilder> fieldDefinitionBuilders = new HashMap<>();
	static {
		fieldDefinitionBuilders.put(Type.BOOLEAN, new BooleanFieldDefinitionBuilder());
		fieldDefinitionBuilders.put(Type.CHECK, new CheckFieldDefinitionBuilder());
		fieldDefinitionBuilders.put(Type.COMPOSITE, new CompositeFieldDefinitionBuilder());
		fieldDefinitionBuilders.put(Type.DATE, new DateFieldDefinitionBuilder());
		fieldDefinitionBuilders.put(Type.FILE, new FileFieldDefinitionBuilder());
		fieldDefinitionBuilders.put(Type.LINK, new LinkFieldDefinitionBuilder());
		fieldDefinitionBuilders.put(Type.MEMO, new MemoFieldDefinitionBuilder());
		fieldDefinitionBuilders.put(Type.NODE, new NodeFieldDefinitionBuilder());
		fieldDefinitionBuilders.put(Type.NUMBER, new NumberFieldDefinitionBuilder());
		fieldDefinitionBuilders.put(Type.PICTURE, new PictureFieldDefinitionBuilder());
		fieldDefinitionBuilders.put(Type.SELECT, new SelectFieldDefinitionBuilder());
		fieldDefinitionBuilders.put(Type.SERIAL, new SerialFieldDefinitionBuilder());
		fieldDefinitionBuilders.put(Type.SUMMATION, new SummationFieldDefinitionBuilder());
		fieldDefinitionBuilders.put(Type.TEXT, new TextFieldDefinitionBuilder());
		fieldDefinitionBuilders.put(Type.URI, new UriFieldDefinitionBuilder());
	};

	@Override
	public T build(HttpInstance instance) {
		if (instance == null)
			return null;

        return (T) fieldDefinitionBuilders.get(Type.fromString(instance.getString("type"))).build(instance);
	}

	@Override
	public void initialize(T object, HttpInstance instance) {
		super.initialize(object, instance);

		FieldDefinition definition = (FieldDefinition) object;

		if (!instance.getString("template").isEmpty())
			definition.setTemplate(Template.fromString(instance.getString("template")));

        if (instance.getObject("boundary") != null)
            ((MultipleableFieldDefinition)definition).setBoundary(createBoundary(instance.getObject("boundary")));

		if (definition instanceof MultipleableFieldDefinition)
			((MultipleableFieldDefinition)definition).setMultiple(instance.getBoolean("multiple"));

		definition.setCollapsible(instance.getBoolean("collapsible"));
		definition.setRequired(instance.getBoolean("required"));
		definition.setReadonly(instance.getBoolean("readonly"));
		definition.setExtended(instance.getBoolean("extended"));
		definition.setSuperField(instance.getBoolean("superField"));
		definition.setStatic(instance.getBoolean("static"));
		definition.setUnivocal(instance.getBoolean("univocal"));
		definition.setDisplays(new DisplayDefinitionBuilder().buildList(instance.getList("displays")));
	}

    private client.core.model.definition.entity.MultipleableFieldDefinition.Boundary createBoundary(final HttpInstance boundary) {
        return new client.core.model.definition.entity.MultipleableFieldDefinition.Boundary() {
            @Override
            public int getMin() {
                return boundary.getInt("min");
            }

            @Override
            public int getMax() {
                return boundary.getInt("max");
            }
        };
    }

    public static class DisplayDefinitionBuilder implements Builder<client.core.model.definition.entity.FieldDefinition.Display, List<client.core.model.definition.entity.FieldDefinition.Display>> {

		@Override
		public Display build(HttpInstance instance) {
			if (instance == null)
				return null;

			FieldDefinition.Display definition = new FieldDefinition.Display();
			initialize(definition, instance);
			return definition;
		}

		@Override
		public void initialize(Display object, HttpInstance instance) {
			FieldDefinition.Display definition = (FieldDefinition.Display) object;
			definition.setMessage(instance.getString("message"));

			if (!instance.getString("when").isEmpty())
				definition.setWhen(FieldDefinition.Display.When.fromString(instance.getString("when")));
		}

		@Override
		public List<Display> buildList(HttpList instance) {
			List<client.core.model.definition.entity.FieldDefinition.Display> result = new MonetList<>();

			for (int i = 0; i < instance.getItems().length(); i++)
				result.add(build(instance.getItems().get(i)));

			return result;
		}

	}


}
