package client.services.http.builders.definition.entity;

import client.core.model.List;
import client.core.model.definition.views.IndexViewDefinition;
import client.core.system.MonetList;
import client.core.system.definition.entity.IndexDefinition;
import client.services.http.HttpInstance;
import client.services.http.builders.definition.RefBuilder;
import client.services.http.builders.definition.entity.views.ViewDefinitionBuilder;
import com.google.gwt.core.client.JsArray;

import static client.core.model.definition.entity.IndexDefinition.ReferenceDefinition.AttributeDefinition.*;

public class IndexDefinitionBuilder extends EntityDefinitionBuilder<client.core.model.definition.entity.IndexDefinition> {

	@Override
	public client.core.model.definition.entity.IndexDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		IndexDefinition definition = new IndexDefinition();
		initialize(definition, instance);
		return definition;
	}

	@Override
	public void initialize(client.core.model.definition.entity.IndexDefinition object, HttpInstance instance) {
		super.initialize(object, instance);

		IndexDefinition definition = (IndexDefinition)object;
		definition.setReference(getReference(instance.getArray("reference")));
		definition.setViews(new ViewDefinitionBuilder<IndexViewDefinition>().buildList(instance.getList("views")));
	}

	private client.core.model.definition.entity.IndexDefinition.ReferenceDefinition getReference(JsArray<HttpInstance> instances) {
		List<client.core.model.definition.entity.IndexDefinition.ReferenceDefinition.AttributeDefinition> attributes = new MonetList<>();

		for (int i = 0; i < instances.length(); i++)
			attributes.add(getReferenceAttribute(instances.get(i)));

		IndexDefinition.ReferenceDefinition reference = new IndexDefinition.ReferenceDefinition();
		reference.setAttributes(attributes);

		return reference;
	}

	private client.core.model.definition.entity.IndexDefinition.ReferenceDefinition.AttributeDefinition getReferenceAttribute(HttpInstance instance) {
		IndexDefinition.ReferenceDefinition.AttributeDefinition attribute = new IndexDefinition.ReferenceDefinition.AttributeDefinition();
		attribute.setCode(instance.getString("code"));
		attribute.setName(instance.getString("name"));
		attribute.setLabel(instance.getString("label"));
		attribute.setDescription(instance.getString("description"));
		attribute.setType(Type.fromString(instance.getString("type")));

		if (!instance.getString("precision").isEmpty())
			attribute.setPrecision(Precision.fromString(instance.getString("precision")));

		if (instance.getObject("source") != null)
			attribute.setSource(new RefBuilder().build(instance.getObject("source")));

		return attribute;
	}

}