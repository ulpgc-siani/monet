package client.core.system;

import client.core.model.*;
import client.core.model.Index;
import client.core.model.definition.Ref;
import client.core.model.definition.entity.IndexDefinition;
import client.core.model.definition.entity.SetDefinition;
import client.core.model.definition.views.SetViewDefinition;

public abstract class Set<Definition extends SetDefinition> extends Node<Definition> {
	protected client.core.model.Index<IndexDefinition> index;

	public Set() {
		super();
	}

	public Set(String id, String label, boolean isComponent, Type type) {
		super(id, label, isComponent, type);
	}

	public List<client.core.model.Filter> loadFilters(String codeView) {
		SetDefinition definition = getDefinition();
		List<client.core.model.Filter> result = new MonetList<>();

		SetViewDefinition viewDefinition = definition.getView(codeView);
		if (viewDefinition == null)
			return result;

		SetViewDefinition.AnalyzeDefinition analyzeDefinition = viewDefinition.getAnalyze();
		if (analyzeDefinition == null || analyzeDefinition.getDimension() == null)
			return result;

		IndexDefinition.ReferenceDefinition referenceDefinition = index.getDefinition().getReference();
		for (Ref ref : analyzeDefinition.getDimension().getAttribute()) {
			IndexDefinition.ReferenceDefinition.AttributeDefinition attributeDefinition = referenceDefinition.getAttribute(ref.getValue());
			result.add(new Filter(attributeDefinition.getCode(), attributeDefinition.getLabel()));
		}

		return result;
	}

	public List<client.core.model.Order> loadOrders(String codeView) {
		SetDefinition definition = getDefinition();
		List<client.core.model.Order> result = new MonetList<>();

		SetViewDefinition viewDefinition = definition.getView(codeView);
		if (viewDefinition == null)
			return result;

		SetViewDefinition.AnalyzeDefinition analyzeDefinition = viewDefinition.getAnalyze();
		if (analyzeDefinition == null || analyzeDefinition.getDimension() == null)
			return result;

		IndexDefinition.ReferenceDefinition referenceDefinition = index.getDefinition().getReference();
		for (Ref ref : analyzeDefinition.getSorting().getAttribute()) {
			IndexDefinition.ReferenceDefinition.AttributeDefinition attributeDefinition = referenceDefinition.getAttribute(ref.getValue());
			result.add(new Order(attributeDefinition.getCode(), attributeDefinition.getLabel()));
		}

		return result;
	}

	public Index<IndexDefinition> getIndex() {
		return index;
	}

	public void setIndex(client.core.model.Index index) {
		this.index = index;
	}

	@Override
	public boolean isEnvironment() {
		return false;
	}

}
