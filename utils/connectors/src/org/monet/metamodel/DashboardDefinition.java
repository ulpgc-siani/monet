package org.monet.metamodel;

import org.monet.metamodel.interfaces.IsInitiable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashboardDefinition extends DashboardDefinitionBase implements IsInitiable {
	HashMap<String, ArrayList<DashboardDefinitionBase.TaxonomyProperty>> ontologiesMap = new HashMap<String, ArrayList<DashboardDefinitionBase.TaxonomyProperty>>();
	DashboardViewProperty defaultView = null;
	HashMap<String, DashboardViewProperty> viewsMap = new HashMap<String, DashboardViewProperty>();
	HashMap<String, IndicatorProperty> indicatorsMap = new HashMap<String, IndicatorProperty>();

	public static class TaxonomyProperty extends DashboardDefinitionBase.TaxonomyProperty {
		public static final String HIERARCHY_SEPARATOR = "@";
	}

	@Override
	public void init() {

		for (DashboardDefinitionBase.TaxonomyProperty taxonomyDefinition : this.getTaxonomyList()) {
			String ontology = taxonomyDefinition.getOntology();

			if (!this.ontologiesMap.containsKey(ontology))
				this.ontologiesMap.put(ontology, new ArrayList<DashboardDefinitionBase.TaxonomyProperty>());

			ArrayList<DashboardDefinitionBase.TaxonomyProperty> taxonomyList = this.ontologiesMap.get(ontology);
			taxonomyList.add(taxonomyDefinition);
		}

		for (DashboardViewProperty view : this._dashboardViewPropertyMap.values()) {
			this.defaultView = view;
			this.viewsMap.put(view.getCode(), view);
			this.viewsMap.put(view.getName(), view);
			break;
		}
		
		for (IndicatorProperty indicatorDefinition : _indicatorPropertyMap.values()) {
			indicatorsMap.put(indicatorDefinition.getCode(), indicatorDefinition);
			indicatorsMap.put(indicatorDefinition.getName(), indicatorDefinition);
		}

	}

	public List<DashboardDefinitionBase.TaxonomyProperty> getTaxonomiesWithOntology(String ontology) {
		if (!this.ontologiesMap.containsKey(ontology)) return new ArrayList<DashboardDefinitionBase.TaxonomyProperty>();
		return this.ontologiesMap.get(ontology);
	}

	public DashboardViewProperty getDefaultView() {
		return this.defaultView;
	}

	public DashboardViewProperty getView(String key) {
		return this.viewsMap.get(key);
	}

	public IndicatorProperty getIndicator(String key) {
		return this.indicatorsMap.get(key);
	}

}