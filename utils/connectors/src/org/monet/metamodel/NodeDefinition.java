package org.monet.metamodel;

import org.monet.api.space.backservice.impl.model.AttributeList;
import org.monet.metamodel.internal.Ref;
import org.monet.v3.model.DefinitionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// NodeDefinition
// Declaración abstracta de un nodo. Un nodo es un elemento navegable del sistema de información

public abstract class NodeDefinition extends NodeDefinitionBase {
	protected NodeViewProperty defaultView;
	protected List<NodeViewProperty> viewList = new ArrayList<NodeViewProperty>();
	protected List<NodeViewProperty> tabViewList = new ArrayList<NodeViewProperty>();
	protected Map<String, NodeViewProperty> viewsMap = new HashMap<String, NodeViewProperty>();
	protected Map<String, RuleProperty> rulesMap = null;

	public boolean isPublic() {
		return this._isPrivate == null;
	}

	public AttributeList buildAttributes() {
		return new AttributeList();
	}

	public static DefinitionType getType(String type) {

		if (type.equals(DefinitionType.catalog.toString()))
			return DefinitionType.catalog;
		else if (type.equals(DefinitionType.collection.toString()))
			return DefinitionType.collection;
		else if (type.equals(DefinitionType.container.toString()))
			return DefinitionType.container;
		else if (type.equals(DefinitionType.desktop.toString()))
			return DefinitionType.desktop;
		else if (type.equals(DefinitionType.document.toString()))
			return DefinitionType.document;
		else if (type.equals(DefinitionType.form.toString()))
			return DefinitionType.form;

		return null;
	}

	public DefinitionType getType() {

		if (this.isCatalog())
			return DefinitionType.catalog;
		else if (this.isCollection())
			return DefinitionType.collection;
		else if (this.isContainer())
			return DefinitionType.container;
		else if (this.isDesktop())
			return DefinitionType.desktop;
		else if (this.isDocument())
			return DefinitionType.document;
		else if (this.isForm())
			return DefinitionType.form;

		return null;
	}

	public boolean isContainer() {
		return this instanceof ContainerDefinition;
	}

	public boolean isDesktop() {
		return this instanceof DesktopDefinition;
	}

	public boolean isCollection() {
		return this instanceof CollectionDefinition;
	}

	public boolean isCatalog() {
		return this instanceof CatalogDefinition;
	}

	public boolean isForm() {
		return this instanceof FormDefinition;
	}

	public boolean isDocument() {
		return this instanceof DocumentDefinition;
	}

	public boolean isPrototypable() {

		if (this instanceof ContainerDefinition) {
			ContainerDefinition definition = (ContainerDefinition) this;
			return definition.isPrototypable();
		}

		if (this instanceof FormDefinition) {
			FormDefinition definition = (FormDefinition) this;
			return definition.isPrototypable();
		}

		return false;
	}

	public boolean isGeoreferenced() {

		if (this instanceof ContainerDefinition) {
			ContainerDefinition definition = (ContainerDefinition) this;
			return definition.getIsGeoreferenced() != null;
		}

		if (this instanceof FormDefinition) {
			FormDefinition definition = (FormDefinition) this;
			return definition.getIsGeoreferenced() != null;
		}

		return false;
	}

	public boolean hasMappings() {

		if (this instanceof FormDefinition) {
			FormDefinition definition = (FormDefinition) this;
			return definition.getMappingList().size() > 0;
		}

		if (this instanceof DocumentDefinition) {
			DocumentDefinition definition = (DocumentDefinition) this;
			return definition.getMappingList().size() > 0;
		}

		return false;
	}

	public NodeViewProperty getDefaultView() {
		return this.defaultView;
	}

	public NodeViewProperty getNodeView(String key) {
		return this.viewsMap.get(key);
	}

	protected void initRulesMap() {
		this.rulesMap = new HashMap<String, RuleProperty>();
		for (RuleNodeProperty rule : this._ruleNodePropertyList)
			this.rulesMap.put(rule.getCode(), rule);
		for (RuleViewProperty rule : this._ruleViewPropertyList)
			this.rulesMap.put(rule.getCode(), rule);
		for (RuleOperationProperty rule : this._ruleOperationPropertyList)
			this.rulesMap.put(rule.getCode(), rule);
	}

	public RuleProperty getRule(String ruleCode) {
		if (this.rulesMap == null)
			initRulesMap();

		return this.rulesMap.get(ruleCode);
	}

	public boolean isComponent() {
		if (this.isDocument()) {
			return ((DocumentDefinition) this).isComponent();
		} else if (this.isCatalog()) {
			return ((CatalogDefinition) this).isComponent();
		} else if (this.isForm()) {
			return ((FormDefinition) this).isComponent();
		} else if (this.isCollection()) {
			return ((CollectionDefinition) this).isComponent();
		}
		return false;
	}

	public List<Ref> getRoles() {
		if (this.isDesktop()) {
			return ((DesktopDefinition) this).getFor().getRole();
		} else if (this.isContainer()) {
			ContainerDefinition definition = (ContainerDefinition) this;
			if (definition.isEnvironment()) {
				return definition.getFor().getRole();
			}
		}
		return null;
	}

	public boolean isEnvironment() {
		if (this.isContainer()) {
			ContainerDefinition definition = (ContainerDefinition) this;
			return definition.isEnvironment();
		}
		return this.isDesktop();
	}

	public boolean requirePartnerContext() {
		return this._requirePartnerContextProperty != null;
	}

	public List<NodeViewProperty> getViewDefinitionList() {
		return this.viewList;
	}

	public List<NodeViewProperty> getTabViewDefinitionList() {
		return this.tabViewList;
	}

}
