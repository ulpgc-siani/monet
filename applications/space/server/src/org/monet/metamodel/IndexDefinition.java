package org.monet.metamodel;

import org.monet.metamodel.IndexDefinitionBase.IndexViewProperty.ShowProperty;
import org.monet.metamodel.interfaces.IsInitiable;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.model.DefinitionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IndexDefinition extends IndexDefinitionBase implements IsInitiable {

	protected IndexViewProperty defaultView;
	protected HashMap<String, AttributeProperty> attributeMap = new HashMap<String, AttributeProperty>();
	protected HashMap<String, IndexViewProperty> viewMap = new HashMap<String, IndexViewProperty>();

	public void init() {
		if (this._referenceProperty != null) {
			for (AttributeProperty attribute : this._referenceProperty._attributePropertyMap.values()) {
				this.attributeMap.put(attribute.getCode(), attribute);
				this.attributeMap.put(attribute.getName(), attribute);
			}
		}
		for (IndexViewProperty view : this._indexViewPropertyMap.values()) {
			this.viewMap.put(view.getCode(), view);
			this.viewMap.put(view.getName(), view);
			if (view.isDefault())
				this.defaultView = view;
		}
	}

	public AttributeProperty getAttribute(String key) {
		return this.attributeMap.get(key);
	}

	public IndexViewProperty getView(String key) {
		return this.viewMap.get(key);
	}

	public IndexViewProperty getDefaultView() {
		return this.defaultView;
	}

	public List<AttributeProperty> getAttributes(IndexViewProperty viewDefinition) {
		List<AttributeProperty> attributes = new ArrayList<AttributeProperty>();
		ShowProperty show = viewDefinition.getShow();

		Ref title = show.getTitle();
		if (title != null)
			attributes.add(this.getAttribute(title.getValue()));

		Ref picture = show.getPicture();
		if (picture != null)
			attributes.add(this.getAttribute(picture.getValue()));

		Ref icon = show.getIcon();
		if (icon != null)
			attributes.add(this.getAttribute(icon.getValue()));

		for (Ref region : show.getHighlight())
			attributes.add(this.getAttribute(region.getValue()));

		for (Ref region : show.getLine())
			attributes.add(this.getAttribute(region.getValue()));

		for (Ref region : show.getLineBelow())
			attributes.add(this.getAttribute(region.getValue()));

		for (Ref region : show.getFooter())
			attributes.add(this.getAttribute(region.getValue()));

		return attributes;
	}

	@Override
	public DefinitionType getType() {
		return DefinitionType.index;
	}
}
