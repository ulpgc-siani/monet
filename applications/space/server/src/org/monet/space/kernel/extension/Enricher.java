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

package org.monet.space.kernel.extension;

import org.monet.metamodel.*;
import org.monet.space.kernel.constants.*;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.Dictionary;

public class Enricher implements IEnricher {
	private HistoryStoreLink historyStoreLink;

	private static Enricher instance;

	private Enricher() {
		this.historyStoreLink = null;
	}

	public synchronized static Enricher getInstance() {
		if (instance == null) instance = new Enricher();
		return instance;
	}

	public Boolean setHistoryStoreLink(HistoryStoreLink dataLink) {
		this.historyStoreLink = dataLink;
		return true;
	}

	public Boolean completeNodeAttributes(FormDefinition formDefinition, Attribute attribute, Integer errors) {
		FieldProperty fieldDefinition = formDefinition.getField(attribute.getCode());

		if (fieldDefinition == null) return false;

		attribute = checkCompatibilityWithAttributeIfSelectField(attribute, fieldDefinition);
		String value = attribute.getIndicatorValue(Common.DataStoreField.VALUE);
		saveToHistory(attribute, value, getDataStore(fieldDefinition), fieldDefinition.getCode());

		if ((fieldDefinition.isRequired()) && (value == null || value.equals(Strings.EMPTY))) errors++;

		for (Attribute childAttribute : attribute.getAttributeList()) {
			completeNodeAttributes(formDefinition, childAttribute, errors);
		}

		return true;
	}

	private Attribute checkCompatibilityWithAttributeIfSelectField(Attribute attribute, FieldProperty fieldDefinition) {
		if (fieldDefinition.isSelect() && !attribute.isMultiple()) {
			return getOptionAttribute(attribute);
		}
		return attribute;
	}

	private Attribute getOptionAttribute(Attribute attribute) {
		return attribute.getAttribute(Attribute.OPTION) != null ? attribute.getAttribute(Attribute.OPTION) : attribute;
	}

	private void saveToHistory(Attribute attribute, String value, String datastore, String fieldCode) {
		if (datastore == null || value == null || value.equals(Strings.EMPTY)) return;
		String indicatorValue = attribute.getIndicatorValue(Common.DataStoreField.CODE);
		historyStoreLink.addTerm(datastore.isEmpty() ? fieldCode : datastore, indicatorValue, value);
	}

	private String getDataStore(FieldProperty fieldDefinition) {
		if (fieldDefinition instanceof TextFieldProperty) {
            TextFieldProperty.EnableHistoryProperty enableHistoryStore = ((TextFieldProperty) fieldDefinition).getEnableHistory();
            return enableHistoryStore != null ? enableHistoryStore.getDatastore() : null;
        }
		if (fieldDefinition instanceof SelectFieldProperty) {
            SelectFieldProperty.EnableHistoryProperty enableHistoryStore = ((SelectFieldProperty) fieldDefinition).getEnableHistory();
            return enableHistoryStore != null ? enableHistoryStore.getDatastore() : null;
        }
		if (fieldDefinition instanceof LinkFieldProperty) {
            LinkFieldProperty.EnableHistoryProperty enableHistoryStore = ((LinkFieldProperty) fieldDefinition).getEnableHistory();
			return enableHistoryStore != null ? enableHistoryStore.getDatastore() : null;
        }
		return null;
	}

	public Boolean completeNode(Node node, TaskList taskList) {
		Session session = Context.getInstance().getCurrentSession();
		Dictionary dictionary = BusinessUnit.getInstance().getBusinessModel().getDictionary();
		Account account = session != null ? session.getAccount() : null;
		Integer numberOfErrors = 0;
		FormDefinition formDefinition;

		if (account == null) return true;
		if (!dictionary.isFormDefinition(node.getCode())) return true;

		taskList.removeOfType(TaskCode.REVISION);
		formDefinition = dictionary.getFormDefinition(node.getCode());

		try {

			for (Attribute attribute : node.getAttributeList().get().values())
				this.completeNodeAttributes(formDefinition, attribute, numberOfErrors);

			if (numberOfErrors > 0) {
				taskList.add(errorTask(node, numberOfErrors));
			}

		} catch (NullPointerException oException) {
			throw new DataException(ErrorCode.COMPLETE_NODE, node.getId(), oException);
		}

		return true;
	}

	private Task errorTask(Node node, int numberOfErrors) {
		Task task = new Task();
		task.setTarget(node);
		task.setCode(TaskCode.REVISION);
		task.setLabel(node.getLabel());
		task.setDescription(taskDescription(numberOfErrors));
		return task;
	}

	private String taskDescription(int numberOfErrors) {
		return Language.getInstance().getMessage(MessageCode.REQUIRED_FIELDS, Language.getCurrent()) +
				"<div class='messages'><div class='errors'>" + numberOfErrors + Strings.SPACE + Language.getInstance().getLabel(LabelCode.ERROR_FIELDS, Language.getCurrent()) + "</div>" +
				"<div class='warnings'>0 " + Language.getInstance().getLabel(LabelCode.WARNING_FIELDS, Language.getCurrent()) + "</div></div>";
	}

}