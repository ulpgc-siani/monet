package org.monet.bpi.java;

import org.monet.api.space.backservice.impl.model.Attribute;
import org.monet.api.space.backservice.impl.model.Indicator;
import org.monet.bpi.FieldText;
import org.monet.metamodel.TextFieldProperty;
import org.monet.metamodel.TextFieldProperty.PatternProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.monet.metamodel.TextFieldProperty.PatternProperty.*;

public class FieldTextImpl extends FieldImpl<String> implements FieldText {

	public static String get(Attribute attribute) {
		FieldTextImpl fieldText = new FieldTextImpl();
		fieldText.attribute = attribute;
		return fieldText.get();
	}

	public static void set(Attribute attribute, TextFieldProperty fieldDefinition, String value) {
		FieldTextImpl fieldText = new FieldTextImpl();
		fieldText.attribute = attribute;
		fieldText.fieldDefinition = fieldDefinition;
		fieldText.set(value);
	}

	@Override
	public String get() {
		return this.getIndicatorValue(Indicator.VALUE);
	}

	@Override
	public void set(String value) {
		TextFieldProperty textFieldDefinition = (TextFieldProperty) this.fieldDefinition;

		if (value == null || value.isEmpty()) {
			if (this.attribute != null) this.attribute.getIndicatorList().clear();
			this.setIndicatorValue(Indicator.VALUE, "");
			value = "";
		}

		value = value.trim();

		if (textFieldDefinition.getPatternList().size() == 0) {
			this.setIndicatorValue(Indicator.VALUE, value);
		} else {
			for (PatternProperty patternDefinition : textFieldDefinition.getPatternList()) {
				Pattern pattern = Pattern.compile(patternDefinition.getRegexp().toString());
				Matcher matcher = pattern.matcher(value);
				if (!matcher.find()) continue;

				ArrayList<PatternProperty.MetaProperty> metaDefinitionList = patternDefinition.getMetaList();
				for (MetaProperty metaDefinition : metaDefinitionList) {
					this.setIndicatorValue(metaDefinition.getIndicator(), matcher.group(metaDefinition.getPosition().intValue()));
				}

				this.setIndicatorValue(Indicator.VALUE, value);
				break;
			}
		}
	}

	@Override
	public boolean equals(Object value) {
		if (value instanceof String)
			return this.get().equals(value);
		else
			return false;
	}

	@Override
	public void clear() {
		this.set("");
	}

	@Override
	public String getGroup(String name) {
		return this.getIndicatorValue(name);
	}

	@Override
	public String getGroup(int position) {
		TextFieldProperty textFieldDeclaration = (TextFieldProperty) this.fieldDefinition;

		for (PatternProperty patternDefinition : textFieldDeclaration.getPatternList()) {
			MetaProperty metaDefinition = patternDefinition.getMetaList().get(position-1);
			if (metaDefinition != null)
				return this.getIndicatorValue(metaDefinition.getIndicator());
		}

		return "";
	}

	@Override
	public Map<String, String> getGroups() {
		Map<String, String> result = new HashMap<>();
		TextFieldProperty textFieldDeclaration = (TextFieldProperty) this.fieldDefinition;

		for (PatternProperty patternDefinition : textFieldDeclaration.getPatternList())
			fillPatternGroups(patternDefinition, result);

		return result;
	}

	private void fillPatternGroups(PatternProperty patternDefinition, Map result) {
		for (MetaProperty metaDefinition : patternDefinition.getMetaList())
			fillPatternGroup(metaDefinition, result);
	}

	private void fillPatternGroup(MetaProperty metaDefinition, Map result) {
		result.put(metaDefinition.getIndicator(), this.getIndicatorValue(metaDefinition.getIndicator()));
	}

}