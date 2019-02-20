package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
ThesaurusDefinition
Un tesauro es un conjunto de términos y representa el vocabulario de la unidad de negocio
Se utilizan para clasificar la información
*/

public  class ThesaurusDefinition extends EntityDefinition {

	
	public static class TermsProperty  {protected ArrayList<TermProperty> _termPropertyList = new ArrayList<TermProperty>();public void addTermProperty(TermProperty value) { _termPropertyList.add(value); }public ArrayList<TermProperty> getTermPropertyList() { return _termPropertyList; }protected void merge(TermsProperty child) {_termPropertyList.addAll(child._termPropertyList);}}protected TermsProperty _termsProperty;public TermsProperty getTerms() { return _termsProperty; }public void setTerms(TermsProperty value) { if(_termsProperty!=null) _termsProperty.merge(value); else {_termsProperty = value;} }
	

	public void merge(ThesaurusDefinition child) {
		super.merge(child);
		
		
		if(_termsProperty == null) _termsProperty = child._termsProperty; else {_termsProperty.merge(child._termsProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return ThesaurusDefinition.class;
	}

}

