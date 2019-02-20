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

package org.monet.space.kernel.constants;

public interface Common {

	Integer NO_DEPTH = -1;
	String DEFAULT_LANGUAGE = "es";

	interface ModelDefinition {
		String FORM = "form";
		String COLLECTION = "collection";
		String CLASSIFIER = "classifier";
	}

	interface FileExtensions {
		String HTML = ".html";
		String XML = ".xml";
		String XSL = ".xsl";
		String LABELS = ".labels.lang";
		String MESSAGES = ".messages.lang";
		String ERROR_MESSAGES = ".errors.lang";
		String DEFINITION = ".xml";
	}

	interface Suffix {
		String VIEW_TITLE = "_TITLE";
		String ARTIFICIAL_ID = "_ART_ID";
	}

	interface UserStatus {
		String PENDING = "-1";
		String ENABLED = "1";
		String DISABLED = "0";
	}

	interface Booleans {
		String TRUE = "1";
		String FALSE = "0";
	}

	interface Genders {
		String MALE = "male";
		String FEMALE = "female";
	}

	interface ObjectProperty {
		String ID = "id";
		String ITEMS = "Items";
	}

	interface Lengths {
		int SHORT_LABEL = 40;
		int SHORT_SECTION = 40;
		int SHORT_DESCRIPTION = 100;
	}

	interface Codes {
		String DUMMY = "dummy";
		String TEMPLATE_MODEL_ROOT = "model";
		String TEMPLATE_MODEL_SELECT_NODE_TYPES = "selectnodetypes";
		String LOG_SUBSCRIBER = "logsubscriber";
		String SERVICES_LISTENER = "serviceslistener";
		String NEWS_POST_LISTENER = "newspostlistener";
		String NOTIFICATIONS_LISTENER = "notificationslistener";
		String BPI = "bpi";
		String RULE_MANAGER_LISTENER = "rulemanagerlistener";
		String PUSH_LISTENER = "pushlistener";
		String MOBILE_PUSH_NOTIFICATIONS_LISTENER = "mobilepushnotificationslistener";
	}

	interface Pages {
		String EXTENSION_HELPER_INDEX_NAME = "index";
	}

	interface Sorting {
		String ATTRIBUTE = "attribute";
		String MODE = "mode";
	}

	interface OrderMode {
		String ASCENDANT = "ascendant";
		String DESCENDANT = "descendant";
	}

	interface ViewModePrefix {
		String VIEW = "view.";
		String EDIT = "edit.";
	}

	interface DataStoreField {
		String CODE = "code";
		String VALUE = "value";
		String BODY = "body";
	}
}