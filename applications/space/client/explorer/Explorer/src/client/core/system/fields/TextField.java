package client.core.system.fields;

import client.core.model.definition.entity.field.TextFieldDefinition;
import client.core.model.definition.entity.field.TextFieldDefinition.EditionDefinition.Mode;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import java.util.HashMap;
import java.util.Map;

public class TextField extends Field<TextFieldDefinition, String> implements client.core.model.fields.TextField {
	private String value;

	public TextField() {
		this(null, null);
	}

	public TextField(String code, String label) {
		this(code, label, "");
	}

	public TextField(String code, String label, String value) {
		super(code, label, Type.TEXT);
		this.value = value;
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.fields.TextField.CLASS_NAME;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String getValueAsString() {
		return getValue();
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public final boolean isMultiple() {
		return false;
	}

	@Override
	public final boolean isNullOrEmpty() {
		return getValue() == null || getValue().isEmpty();
	}

    @Override
    public Map<String, String> getMetaData() {
        return calculateMetaData(getValue());
    }

    @Override
	public final String[] getMetaDataValues() {
		Map<String, String> metaData = calculateMetaData(getValue());
		return getMetaData().values().toArray(new String[metaData.size()]);
	}

	@Override
	public final String getMeta(String indicator) {
		Map<String, String> metaData = calculateMetaData(getValue());
		return !metaData.containsKey(indicator) ? null : metaData.get(indicator);
	}

	@Override
	public boolean metaDataIsValid(String value) {
		if (value.isEmpty() || getDefinition() == null || getDefinition().getPatterns() == null || getDefinition().getPatterns().isEmpty()) return true;
		for (TextFieldDefinition.PatternDefinition patternDefinition : getDefinition().getPatterns())
			if (value.matches(patternDefinition.getRegExp()))
				return true;
		return false;
	}

	@Override
	public final Mode getMode() {
		TextFieldDefinition definition = getDefinition();
		if (definition == null || definition.getEdition() == null) return null;
		return definition.getEdition().getMode();
	}

	private Map<String, String> calculateMetaData(String value) {
		TextFieldDefinition definition = getDefinition();
        if (definition == null)
			return new HashMap<>();

		for (TextFieldDefinition.PatternDefinition patternDefinition : definition.getPatterns()) {
            MatchResult matchResult = RegExp.compile(patternDefinition.getRegExp()).exec(value);
			if (matchResult != null)
                return createMeta(patternDefinition, matchResult);
		}

		return new HashMap<>();
	}

    private Map<String, String> createMeta(TextFieldDefinition.PatternDefinition patternDefinition, MatchResult matchResult) {
        final Map<String, String> result = new HashMap<>();
        int index = 1;
        for (TextFieldDefinition.PatternDefinition.MetaDefinition meta : patternDefinition.getMetaList())
            result.put(meta.getIndicator(), matchResult.getGroupCount() > index ? matchResult.getGroup(index++) : "");
        return result;
    }

}
