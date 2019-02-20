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

package org.monet.space.kernel.model;

import com.google.common.collect.Iterables;
import org.monet.metamodel.FieldProperty;
import org.monet.metamodel.FormDefinition;
import org.monet.metamodel.NodeDefinition;

public class NodeDefinitionChangesResolver implements ModelChangesResolver<Attribute> {
	private final NodeDefinition scope;

	public NodeDefinitionChangesResolver(NodeDefinition scope) {
		this.scope = scope;
	}

	@Override
	public void resolve(Attribute value) {
		resolveForm(scope, value);
	}

	private void resolveForm(NodeDefinition scope, Attribute value) {
		if (!scope.isForm())
			return;

		resolveFieldCardinality(((FormDefinition)scope), value);
	}

	private void resolveFieldCardinality(FormDefinition scope, Attribute value) {
		FieldProperty definition = scope.getField(value.getCode());

		if (definition == null)
			return;

		if (value.isMultipleChild())
			return;

		if (isMultiple(value)) {
			if (!definition.isMultiple()) convertToSingle(definition, value);
		}
		else
			if (definition.isMultiple()) convertToMultiple(definition, value);
	}

	private boolean isMultiple(Attribute value) {
		return value.getAttributeList().getByCode(value.getCode()).size() > 0;
	}

	private void convertToMultiple(FieldProperty definition, Attribute value) {
		if (definition.isComposite())
			convertCompositeToMultiple(value);
		else
			convertGenericToMultiple(value);
	}

	private void convertCompositeToMultiple(Attribute value) {
		if (value.getAttributeList().getCount() == 0) return;

		Attribute attribute = createAttribute(value);
		attribute.setIndicatorList(new IndicatorList(value.getIndicatorList()));
		attribute.setAttributeList(new AttributeList(value.getAttributeList()));
		value.getAttributeList().clear();
		value.getAttributeList().add(attribute);
	}

	private void convertGenericToMultiple(Attribute value) {
		if (value.getIndicatorList().getCount() == 0) return;

		Attribute attribute = createAttribute(value);
		attribute.setIndicatorList(new IndicatorList(value.getIndicatorList()));
		value.getIndicatorList().clear();
		value.getAttributeList().clear();
		value.getAttributeList().add(attribute);
	}

	private Attribute createAttribute(Attribute value) {
		Attribute attribute = new Attribute();
		attribute.setParentCode(value.getCode());
		attribute.setCode(value.getCode());
		return attribute;
	}

	private void convertToSingle(FieldProperty definition, Attribute value) {
		if (definition.isComposite())
			convertCompositeToSingle(value);
		else
			convertGenericToSingle(value);
	}

	private void convertCompositeToSingle(Attribute value) {
		Attribute attribute = Iterables.get(value.getAttributeList().get().values(), 0);
		value.setIndicatorList(new IndicatorList(attribute.getIndicatorList()));
		value.setAttributeList(new AttributeList(attribute.getAttributeList()));
	}

	private void convertGenericToSingle(Attribute value) {
		Attribute attribute = Iterables.get(value.getAttributeList().get().values(), 0);
		value.setIndicatorList(new IndicatorList(attribute.getIndicatorList()));
		value.setAttributeList(new AttributeList());
	}
}
