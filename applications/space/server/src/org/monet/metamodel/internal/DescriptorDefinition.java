/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package org.monet.metamodel.internal;

import org.monet.bpi.java.PropertiesImpl;
import org.monet.metamodel.AttributeProperty;
import org.monet.metamodel.IndexDefinition;
import org.monet.metamodel.interfaces.HasBehaviour;

public class DescriptorDefinition extends IndexDefinition implements HasBehaviour {

	public static final String ATTRIBUTE_ID_NODE = "id_node";
	public static final String ATTRIBUTE_EDITABLE = "editable";
	public static final String ATTRIBUTE_DELETABLE = "deletable";
	public static final String ATTRIBUTE_ID_OWNER = "id_owner";
	public static final String ATTRIBUTE_ID_PARENT = "id_parent";
	public static final String ATTRIBUTE_CODE = "code";
	public static final String ATTRIBUTE_LABEL = "label";
	public static final String ATTRIBUTE_DESCRIPTION = "description";
	public static final String ATTRIBUTE_COLOR = "color";
	public static final String ATTRIBUTE_ORDERING = "ordering";
	public static final String ATTRIBUTE_CREATE_DATE = "create_date";
	public static final String ATTRIBUTE_UPDATE_DATE = "update_date";
	public static final String ATTRIBUTE_DELETE_DATE = "delete_date";
	public static final String ATTRIBUTE_HIGHLIGHTED = "highlighted";
	public static final String ATTRIBUTE_PROTOTYPE = "prototype";
	public static final String ATTRIBUTE_GEOREFERENCED = "georeferenced";

	public static final String CODE = "td";

	public static class DescriptorAttributeProperty extends AttributeProperty {

		public DescriptorAttributeProperty(String code, AttributeProperty.TypeEnumeration type) {
			this._code = code;
			this._name = code;
			this._type = type;
		}

	}

	public DescriptorDefinition() {
		this._code = DescriptorDefinition.CODE;
		this._referenceProperty = new ReferenceProperty();
		this.addAttribute(DescriptorDefinition.ATTRIBUTE_ID_NODE, new DescriptorAttributeProperty(DescriptorDefinition.ATTRIBUTE_ID_NODE, AttributeProperty.TypeEnumeration.INTEGER));
		this.addAttribute(DescriptorDefinition.ATTRIBUTE_EDITABLE, new DescriptorAttributeProperty(DescriptorDefinition.ATTRIBUTE_EDITABLE, AttributeProperty.TypeEnumeration.BOOLEAN));
		this.addAttribute(DescriptorDefinition.ATTRIBUTE_DELETABLE, new DescriptorAttributeProperty(DescriptorDefinition.ATTRIBUTE_DELETABLE, AttributeProperty.TypeEnumeration.BOOLEAN));
		this.addAttribute(DescriptorDefinition.ATTRIBUTE_ID_OWNER, new DescriptorAttributeProperty(DescriptorDefinition.ATTRIBUTE_ID_OWNER, AttributeProperty.TypeEnumeration.INTEGER));
		this.addAttribute(DescriptorDefinition.ATTRIBUTE_ID_PARENT, new DescriptorAttributeProperty(DescriptorDefinition.ATTRIBUTE_ID_PARENT, AttributeProperty.TypeEnumeration.INTEGER));
		this.addAttribute(DescriptorDefinition.ATTRIBUTE_CODE, new DescriptorAttributeProperty(DescriptorDefinition.ATTRIBUTE_CODE, AttributeProperty.TypeEnumeration.STRING));
		this.addAttribute(DescriptorDefinition.ATTRIBUTE_LABEL, new DescriptorAttributeProperty(DescriptorDefinition.ATTRIBUTE_LABEL, AttributeProperty.TypeEnumeration.STRING));
		this.addAttribute(DescriptorDefinition.ATTRIBUTE_DESCRIPTION, new DescriptorAttributeProperty(DescriptorDefinition.ATTRIBUTE_DESCRIPTION, AttributeProperty.TypeEnumeration.STRING));
		this.addAttribute(DescriptorDefinition.ATTRIBUTE_COLOR, new DescriptorAttributeProperty(DescriptorDefinition.ATTRIBUTE_COLOR, AttributeProperty.TypeEnumeration.STRING));
		this.addAttribute(DescriptorDefinition.ATTRIBUTE_ORDERING, new DescriptorAttributeProperty(DescriptorDefinition.ATTRIBUTE_ORDERING, AttributeProperty.TypeEnumeration.INTEGER));
		this.addAttribute(DescriptorDefinition.ATTRIBUTE_CREATE_DATE, new DescriptorAttributeProperty(DescriptorDefinition.ATTRIBUTE_CREATE_DATE, AttributeProperty.TypeEnumeration.DATE));
		this.addAttribute(DescriptorDefinition.ATTRIBUTE_UPDATE_DATE, new DescriptorAttributeProperty(DescriptorDefinition.ATTRIBUTE_UPDATE_DATE, AttributeProperty.TypeEnumeration.DATE));
		this.addAttribute(DescriptorDefinition.ATTRIBUTE_DELETE_DATE, new DescriptorAttributeProperty(DescriptorDefinition.ATTRIBUTE_DELETE_DATE, AttributeProperty.TypeEnumeration.DATE));
		this.addAttribute(DescriptorDefinition.ATTRIBUTE_HIGHLIGHTED, new DescriptorAttributeProperty(DescriptorDefinition.ATTRIBUTE_HIGHLIGHTED, AttributeProperty.TypeEnumeration.BOOLEAN));
		this.addAttribute(DescriptorDefinition.ATTRIBUTE_PROTOTYPE, new DescriptorAttributeProperty(DescriptorDefinition.ATTRIBUTE_PROTOTYPE, AttributeProperty.TypeEnumeration.BOOLEAN));
		this.addAttribute(DescriptorDefinition.ATTRIBUTE_GEOREFERENCED, new DescriptorAttributeProperty(DescriptorDefinition.ATTRIBUTE_GEOREFERENCED, AttributeProperty.TypeEnumeration.INTEGER));
	}

	private void addAttribute(String code, AttributeProperty attributeProperty) {
		this._referenceProperty.addAttributeProperty(attributeProperty);
		this.attributeMap.put(code, attributeProperty);
	}

	@Override
	public Class<?> getBehaviourClass() {
		return PropertiesImpl.class;
	}

}