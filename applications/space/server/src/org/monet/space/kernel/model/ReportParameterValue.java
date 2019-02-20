package org.monet.space.kernel.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReportParameterValue {
	public static final int TEXT_MULTI = 0;
	public static final int TEXT_SINGLE = 1;
	public static final int DATE = 2;
	public static final int NUMBER = 3;

	private String label;
	private String engineName;
	private int codeType;

	private List<String> values;
	private HashMap<String, String> engineValues;
	private String thesaurus;
	private String dimension;


	public ReportParameterValue() {
		values = new ArrayList<String>();
		engineValues = new HashMap<String, String>();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getEngineName() {
		return engineName;
	}

	public void setEngineName(String engineName) {
		this.engineName = engineName;
	}

	public int getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeTypeString) {
		if (codeTypeString.equalsIgnoreCase("select")) this.codeType = TEXT_MULTI;
		else if (codeTypeString.equalsIgnoreCase("select-single")) this.codeType = TEXT_SINGLE;
		else if (codeTypeString.equalsIgnoreCase("date")) this.codeType = DATE;
	}

	public String getThesaurus() {
		return thesaurus;
	}

	public void setThesaurus(String thesaurus) {
		this.thesaurus = thesaurus;
	}

	public HashMap<String, String> getEngineValue() {
		return engineValues;
	}

	public void addValue(String value, String engineValue) {
		values.add(value);
		this.engineValues.put(value, engineValue);
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public String getDimension() {
		return dimension;
	}
}
