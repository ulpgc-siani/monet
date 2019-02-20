package client.services.http.builders.definition.entity.field;

import client.core.model.List;
import client.core.system.MonetList;
import client.core.system.definition.entity.field.TextFieldDefinition;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsArray;

import static client.core.model.definition.entity.field.TextFieldDefinition.EditionDefinition.Mode;

public class TextFieldDefinitionBuilder extends FieldDefinitionBuilder<client.core.model.definition.entity.field.TextFieldDefinition> {

	@Override
	public client.core.model.definition.entity.field.TextFieldDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		TextFieldDefinition definition = new TextFieldDefinition();
		initialize(definition, instance);
		return definition;
	}

	@Override
	public void initialize(client.core.model.definition.entity.field.TextFieldDefinition object, HttpInstance instance) {
		super.initialize(object, instance);

		TextFieldDefinition definition = (TextFieldDefinition)object;
		definition.setAllowHistory(getAllowHistory(instance.getObject("allowHistory")));
		definition.setLength(getLength(instance.getObject("length")));
		definition.setEdition(getEdition(instance.getObject("edition")));
		definition.setPatterns(getPatterns(instance));
	}

	private client.core.model.definition.entity.field.TextFieldDefinition.AllowHistoryDefinition getAllowHistory(HttpInstance instance) {
		if (instance == null)
			return null;

		TextFieldDefinition.AllowHistoryDefinition allowHistory = new TextFieldDefinition.AllowHistoryDefinition();
		allowHistory.setDataStore(instance.getString("dataStore"));
		return allowHistory;
	}

	private client.core.model.definition.entity.field.TextFieldDefinition.LengthDefinition getLength(HttpInstance instance) {
		if (instance == null)
			return null;

		TextFieldDefinition.LengthDefinition length = new TextFieldDefinition.LengthDefinition();
		length.setMin(instance.getInt("min"));
		length.setMax(instance.getInt("max"));

		return length;
	}

	private client.core.model.definition.entity.field.TextFieldDefinition.EditionDefinition getEdition(HttpInstance instance) {
		if (instance == null)
			return null;

		TextFieldDefinition.EditionDefinition edition = new TextFieldDefinition.EditionDefinition();
		edition.setMode(Mode.fromString(instance.getString("mode")));

		return edition;
	}

	private List<client.core.model.definition.entity.field.TextFieldDefinition.PatternDefinition> getPatterns(HttpInstance instance) {
		List<client.core.model.definition.entity.field.TextFieldDefinition.PatternDefinition> result = new MonetList<>();

        if (instance.getBoolean("mail"))
            result.add(emailPattern());

		for (int i = 0; i < instance.getArray("patterns").length(); i++)
			result.add(getPattern(instance.getArray("patterns").get(i)));

		return result;
	}

    private client.core.model.definition.entity.field.TextFieldDefinition.PatternDefinition emailPattern() {
        final TextFieldDefinition.PatternDefinition patternDefinition = new TextFieldDefinition.PatternDefinition();
        patternDefinition.setRegExp("^[_a-z0-9-]+(.[_a-z0-9-]+)*@[a-z0-9-]+(.[a-z0-9-]+)*(.[a-z]{2,4})$");
        patternDefinition.setMetaList(new MonetList<client.core.model.definition.entity.field.TextFieldDefinition.PatternDefinition.MetaDefinition>());
        return patternDefinition;
    }

    private client.core.model.definition.entity.field.TextFieldDefinition.PatternDefinition getPattern(HttpInstance instance) {

		TextFieldDefinition.PatternDefinition pattern = new TextFieldDefinition.PatternDefinition();
		pattern.setRegExp(instance.getString("regExp"));
		pattern.setMetaList(getMetaList(instance.getArray("metas")));

		return pattern;
	}

	private List<client.core.model.definition.entity.field.TextFieldDefinition.PatternDefinition.MetaDefinition> getMetaList(JsArray<HttpInstance> instances) {
		List<client.core.model.definition.entity.field.TextFieldDefinition.PatternDefinition.MetaDefinition> result = new MonetList<>();

		for (int i = 0; i < instances.length(); i++)
			result.add(getMeta(instances.get(i)));

		return result;
	}

	private client.core.model.definition.entity.field.TextFieldDefinition.PatternDefinition.MetaDefinition getMeta(HttpInstance instance) {
		TextFieldDefinition.PatternDefinition.MetaDefinition meta = new TextFieldDefinition.PatternDefinition.MetaDefinition();
		meta.setIndicator(instance.getString("indicator"));
		meta.setPosition(instance.getLong("position"));
		return meta;
	}

}
